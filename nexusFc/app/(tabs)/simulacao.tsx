import { empty } from '@/constants';
import useAsyncFetch from '@/hooks/use-async-fetch';
import useCurrentUser from '@/hooks/use-current-user';
import professionalTeams, {
  ProfessionalTeam,
} from '@/services/professional-teams';
import simulations from '@/services/simulations';
import UserService, { UserTeam } from '@/services/user';
import { ObjectId } from '@/utils/types/utils';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { router } from 'expo-router';
import React, { useCallback, useEffect, useMemo, useState } from 'react';
import {
  Alert,
  Image,
  Modal,
  ScrollView,
  ToastAndroid,
  TouchableOpacity,
} from 'react-native';
import { View, Text } from 'react-native';
import {
  GestureHandlerRootView,
  RefreshControl,
} from 'react-native-gesture-handler';
import { SafeAreaView } from 'react-native-safe-area-context';
import { random } from 'lodash';

const Simulacao = () => {
  const [currentUser] = useCurrentUser();
  const [userTeam, setUserTeam] = useState<UserTeam | null>(null);
  const [refreshing, setRefreshing] = useState(false);
  const [openSelectAdversary, setOpenSelectAdversary] = useState(false);
  const [selectTeam, setSelectTeam] = useState<ProfessionalTeam | undefined>(
    undefined,
  );

  const simulateGame = useCallback(async () => {
    if (!selectTeam && (currentUser?._id || currentUser?.id)) return;

    try {
      const data = await simulations.post({
        challengerId: currentUser?.id || currentUser?._id,
        challengedId: selectTeam?.id,
        versusPlayer: false,
        betValue: 0.0,
      });

      console.log('data', data);
      const sleep = (t: number) =>
        new Promise((resolve) => setTimeout(resolve, t));

      const realResult = random(0, 1);
      const result = ['win', 'lose'];
      const resultSimulation = result[realResult];

      await sleep(5000);
      const message =
        resultSimulation === 'win'
          ? `Parabéns!! Você venceu sua partida contra a ${selectTeam?.name}.`
          : `Quase!! Você perdeu sua partida contra a ${selectTeam?.name}.`;

      const title = resultSimulation === 'win' ? 'Vitória' : 'Derrota';

      Alert.alert(title, message, [{ text: 'Ok' }]);
    } catch (e) {
      console.log(e);
      ToastAndroid.show('Falha ao Simular a partida.', ToastAndroid.LONG);
    }
  }, [currentUser, selectTeam]);

  const fetchUserTeam = useCallback(async () => {
    if (!currentUser?._id && !currentUser?.id) return;

    try {
      const data = await UserService.getTeam(
        currentUser?._id || currentUser?.id,
      );
      setUserTeam(data);
    } catch (e) {
      console.log(e);
      ToastAndroid.show('Falha ao carregar o time.', ToastAndroid.LONG);
    }
  }, [currentUser]);

  const playersHeader = useMemo(() => {
    if (!userTeam) {
      return [];
    }

    return userTeam.professionalPlayers.map((userPlayers) => {
      const match = userPlayers.player.nick.match(/^\w+/);
      const nickname = match ? match[0] : userPlayers.player.nick;

      return {
        nick: nickname,
        image: userPlayers.player.imageUrl,
      };
    });
  }, [userTeam]);

  const { data: teamsList = empty.array } = useAsyncFetch(
    {
      callback: async () => {
        if (!currentUser) return;

        return await professionalTeams.get();
      },
      errorMessage: 'Falha ao carregar o histórico de partidas.',
    },
    [currentUser],
  );

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    await fetchUserTeam();
    setRefreshing(false);
  }, [fetchUserTeam]);

  useEffect(() => {
    fetchUserTeam();
  }, []);

  return (
    <GestureHandlerRootView>
      <SafeAreaView
        edges={['bottom', 'left', 'right', 'top']}
        style={{
          flex: 1,
          backgroundColor: '#0A131D',
          padding: 15,
        }}
      >
        <ScrollView
          contentContainerStyle={{
            width: '100%',
          }}
          refreshControl={
            <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
          }
        >
          <View style={{ flex: 1, marginTop: 20, alignItems: 'center' }}>
            <Text
              style={{
                color: 'white',
                fontSize: 36,
                borderRadius: 10,
                marginBottom: 15,
                backgroundColor: '#c49b3b',
              }}
            >
              {userTeam?.name}
            </Text>
            <View
              style={{
                flexDirection: 'row',
                justifyContent: 'space-between',
                padding: 15,
                flex: 1,
                borderWidth: 1,
                borderColor: '#c49b3b',
              }}
            >
              {playersHeader.length ? (
                playersHeader.map((player, index) => {
                  return (
                    <View
                      key={index}
                      style={{
                        alignItems: 'center',
                        flexDirection: 'column',
                        paddingTop: 20,
                        flex: 1,
                      }}
                    >
                      <Image
                        source={{ uri: player.image }}
                        style={{
                          width: 60,
                          height: 60,
                          marginBottom: 8,
                          margin: 5,
                          borderRadius: 30,
                          backgroundColor: '#222',
                        }}
                      />
                      <Text
                        style={{
                          color: 'white',
                          textAlign: 'center',
                          fontSize: 11,
                        }}
                        textBreakStrategy="balanced"
                        numberOfLines={1}
                        adjustsFontSizeToFit
                        lineBreakMode="tail"
                      >
                        {player.nick}
                      </Text>
                    </View>
                  );
                })
              ) : (
                <TouchableOpacity
                  style={{
                    flex: 1,
                    alignItems: 'center',
                    margin: 10,
                    borderRadius: 8,
                  }}
                  onPress={() => router.navigate('/(tabs)/escalacao')}
                >
                  <Text
                    style={{
                      color: 'white',
                      padding: 8,
                      fontSize: 18,
                    }}
                  >
                    Nenhum jogador cadastrado no time.
                  </Text>

                  <Text
                    style={{
                      color: 'white',
                      padding: 8,
                      fontSize: 14,
                    }}
                  >
                    Cadastre seu time na aba de Escalação.
                  </Text>
                </TouchableOpacity>
              )}
            </View>

            <View
              style={{
                flex: 1,
                marginVertical: 40,
                alignItems: 'center',
              }}
            >
              <MaterialCommunityIcons
                name="sword-cross"
                size={50}
                color="red"
              />
            </View>

            {selectTeam && (
              <View style={{ flex: 1, marginTop: 20, alignItems: 'center' }}>
                <Text
                  style={{
                    color: 'white',
                    fontSize: 36,
                    borderRadius: 10,
                    marginBottom: 15,
                  }}
                >
                  Adversário:
                </Text>
                <Text
                  style={{
                    color: 'white',
                    fontSize: 36,
                    borderRadius: 10,
                    marginBottom: 15,
                  }}
                >
                  {selectTeam?.name}
                </Text>
              </View>
            )}

            {!selectTeam ? (
              <TouchableOpacity
                style={{
                  flex: 1,
                  marginTop: 10,
                  alignItems: 'center',
                  backgroundColor: '#c49b3b',
                  borderRadius: 10,
                  padding: 20,
                }}
                onPress={() => {
                  setOpenSelectAdversary(true);
                }}
              >
                <Text style={{ color: 'white', padding: 8, fontSize: 14 }}>
                  Escolher adversário
                </Text>
              </TouchableOpacity>
            ) : (
              <View style={{ flex: 1, flexDirection: 'row' }}>
                <TouchableOpacity
                  style={{
                    flex: 1,
                    marginTop: 10,
                    alignItems: 'center',
                    backgroundColor: '#6F89FA',
                    borderRadius: 10,
                    padding: 10,
                    marginHorizontal: 5,
                  }}
                  onPress={() => {
                    simulateGame();
                  }}
                >
                  <Text style={{ color: 'white', padding: 8, fontSize: 14 }}>
                    Simular
                  </Text>
                </TouchableOpacity>
                <TouchableOpacity
                  style={{
                    flex: 1,
                    marginTop: 10,
                    alignItems: 'center',
                    backgroundColor: '#c49b3b',
                    borderRadius: 10,
                    padding: 10,
                  }}
                  onPress={() => {
                    setOpenSelectAdversary(true);
                  }}
                >
                  <Text style={{ color: 'white', padding: 8, fontSize: 14 }}>
                    Escolher adversário
                  </Text>
                </TouchableOpacity>
              </View>
            )}
          </View>
        </ScrollView>

        <Modal
          transparent={true} // Define um fundo semi-transparente
          visible={openSelectAdversary}
          animationType="slide" // Animação ao abrir/fechar
          onRequestClose={() => setOpenSelectAdversary(false)} // Fecha ao pressionar "Voltar" no Android
        >
          <View
            style={{
              flex: 1,
              justifyContent: 'center',
              alignItems: 'center',
              backgroundColor: 'rgba(0, 0, 0, 0.5)', // Fundo semi-transparente
            }}
          >
            <View
              style={{
                width: '80%',
                maxHeight: '70%',
                padding: 10,
                backgroundColor: 'white',
                borderRadius: 10,
                alignItems: 'center',
                shadowColor: '#000',
                shadowOffset: { width: 0, height: 2 },
                shadowOpacity: 0.25,
                shadowRadius: 4,
                elevation: 5,
              }}
            >
              <ScrollView>
                {teamsList.length ? (
                  teamsList.map((team, index) => (
                    <View style={{ width: '100%' }}>
                      <TouchableOpacity
                        onPress={() => {
                          setOpenSelectAdversary(false);
                          setSelectTeam(team);
                        }}
                      >
                        <Text
                          style={{
                            fontSize: 18,
                            fontWeight: 'semibold',
                            marginBottom: 10,
                            paddingVertical: 10,
                            paddingHorizontal: 15,
                            borderWidth: 1,
                            borderRadius: 8,
                            borderColor: 'black',
                          }}
                        >
                          {index}. {team.name}
                        </Text>
                      </TouchableOpacity>
                    </View>
                  ))
                ) : (
                  <Text
                    style={{
                      fontSize: 18,
                      fontWeight: 'bold',
                      marginBottom: 10,
                    }}
                  >
                    Nenhum time disponível no momento.
                  </Text>
                )}

                {/* Botão para fechar a modal */}
                <TouchableOpacity
                  onPress={() => setOpenSelectAdversary(false)}
                  style={{
                    marginTop: 20,
                    padding: 10,
                    backgroundColor: '#c49b3b',
                    borderRadius: 8,
                  }}
                >
                  <Text style={{ color: 'white', fontWeight: 'bold' }}>
                    Fechar
                  </Text>
                </TouchableOpacity>
              </ScrollView>
            </View>
          </View>
        </Modal>
      </SafeAreaView>
    </GestureHandlerRootView>
  );
};

export default Simulacao;
