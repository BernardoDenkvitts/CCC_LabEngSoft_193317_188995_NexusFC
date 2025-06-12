import useAsyncFetch from '@/hooks/use-async-fetch';
import useCurrentUser from '@/hooks/use-current-user';
import {
  Feather,
  FontAwesome,
  FontAwesome5,
  FontAwesome6,
  Ionicons,
  MaterialCommunityIcons,
} from '@expo/vector-icons';
import React, { useEffect, useMemo, useState } from 'react';
import {
  View,
  Text,
  Image,
  StyleSheet,
  ScrollView,
  ToastAndroid,
  TouchableOpacity,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import UserService, { UserTeam } from '@/services/user';
import ProfessionalPlayersService, {
  ProfessionalPlayer,
} from '@/services/professional-players';
import { empty } from '@/constants';
import simulations, { Simulation } from '@/services/simulations';
import professionalTeams from '@/services/professional-teams';
import { ObjectId } from '@/utils/types/utils';
import Button from '@/components/button';
import { router } from 'expo-router';

const teams = [
  { name: 'SKT TELECOM', image: '' },
  { name: 'HUAWEI LIFE', image: '' },
  { name: 'GEN G ACADEMY', image: '' },
];

const matchs = [
  { team1: 'SKT TELECOM', image1: '', team2: 'qualquer time', image2: '' },
  { team1: 'HUAWEI LIFE', image1: '', team2: 'qualquer time', image2: '' },
  { team1: 'GEN G ACADEMY', image1: '', team2: 'qualquer time', image2: '' },
];

type Matches = {
  simulationId: ObjectId;
  win: boolean | null;
  challengerTeamName: string;
  challengedTeamName: string;
};

const logoLPL = require('../../assets/images/lpl-logo.png');

const HomeScreen = () => {
  const [currentUser] = useCurrentUser();
  const [userTeam, setUserTeam] = useState<UserTeam | null>(null);
  const [loading, setLoading] = useState(false);
  const [matches, setMatches] = useState<Matches[]>([]);

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

  const { data: simulationsHistory = empty.array } = useAsyncFetch(
    {
      callback: async () => {
        if (!currentUser) return;

        return await simulations.history('681d5c53e82a0d3f43e82aa6');
      },
      errorMessage: 'Falha ao carregar o histórico de partidas.',
    },
    [currentUser],
  );

  useEffect(() => {
    if (!currentUser?._id || simulationsHistory.length === 0) return;

    const fetchTeam = async (id: string) => {
      try {
        const userTeam = await UserService.getTeam(id);
        if (userTeam?.name) return userTeam.name;
      } catch (e) {
        console.log(e);
      }

      try {
        const professionalTeam = await professionalTeams.find(id);
        if (professionalTeam[0]?.name) return professionalTeam[0].name;
      } catch (e) {
        console.log(e);
      }

      return 'Time não encontrado';
    };

    const fetchTeamNames = async () => {
      setLoading(true);

      try {
        const teamNameResults = await Promise.all(
          simulationsHistory.map(async (simulation) => {
            const [challengerTeamName, challengedTeamName] = await Promise.all([
              fetchTeam(simulation.challengerId),
              fetchTeam(simulation.challengedId),
            ]);

            return {
              simulationId: simulation.id,
              win: simulation.win,
              challengerTeamName,
              challengedTeamName,
            };
          }),
        );

        setMatches(teamNameResults);
      } catch (e) {
        console.log(e);
        ToastAndroid.show(
          'Falha ao carregar os nomes dos times.',
          ToastAndroid.LONG,
        );
      } finally {
        setLoading(false);
      }
    };

    fetchTeamNames();
  }, [currentUser, simulationsHistory]);

  useEffect(() => {
    if (!currentUser?._id) return;

    const fetchHistory = async () => {
      setLoading(true);

      try {
        const data = await UserService.getTeam(currentUser._id);
        setUserTeam(data);
      } catch (e) {
        console.log(e);
        ToastAndroid.show('Falha ao carregar o time.', ToastAndroid.LONG);
      } finally {
        setLoading(false);
      }
    };

    fetchHistory();
  }, [currentUser, setLoading]);

  return (
    <SafeAreaView
      edges={['bottom', 'left', 'right', 'top']}
      style={{ flex: 1, backgroundColor: '#0A131D' }}
    >
      <ScrollView contentContainerStyle={styles.container}>
        <View
          style={{
            paddingHorizontal: 5,
            paddingTop: 20,
            borderRadius: 15,
          }}
        >
          <View
            style={{ borderWidth: 1, borderRadius: 12, borderColor: '#f0c420' }}
          >
            <View style={styles.inputWrapper}>
              <Text style={styles.teamLabel}>{userTeam?.name}</Text>
              <View style={styles.playersContainer}>
                {playersHeader.length ? (
                  playersHeader.map((player, index) => {
                    return (
                      <View key={index} style={styles.playerCard}>
                        <FontAwesome5 name="user" size={40} color={'white'} />
                        {/* <Image source={player.player.ima} style={styles.playerImage} /> */}
                        <Text
                          style={styles.memberName}
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
                      flexDirection: 'row',
                      alignItems: 'center',
                      margin: 10,
                      borderRadius: 8,
                      backgroundColor: '#c49b3b',
                    }}
                    onPress={() => router.navigate('/(tabs)/escalacao')}
                  >
                    <Text
                      style={{
                        color: 'white',
                        padding: 8,
                        fontSize: 16,
                      }}
                    >
                      Nenhum jogador cadastrado no time.
                    </Text>
                    <Feather name="edit" size={28} color="white" />
                  </TouchableOpacity>
                )}
              </View>
              <View style={styles.statsContainer}>
                <View style={styles.statBox}>
                  <MaterialCommunityIcons
                    name="star-four-points"
                    size={20}
                    color="#f0c420"
                  />
                  <Text style={styles.statValue}>
                    {currentUser?.coins || 0}
                  </Text>
                </View>
                <View style={styles.statBox}>
                  <MaterialCommunityIcons
                    name="trophy-outline"
                    size={24}
                    color="#f0c420"
                  />
                  <Text style={styles.statValue}>
                    {userTeam?.pontos || 0 + '/' + 0}
                  </Text>
                </View>
              </View>
            </View>
          </View>
        </View>

        <View
          style={{
            flex: 1,
            flexDirection: 'row',
            alignItems: 'center',
            marginTop: 20,
          }}
        >
          <View
            style={{
              flex: 1,
              height: 2,
              backgroundColor: '#f0c420',
            }}
          />

          <View style={{ flex: 1, alignItems: 'center' }}>
            <Image
              source={logoLPL}
              style={{
                borderColor: '#f0c420',
                borderWidth: 2,
                width: 60,
                height: 60,
                borderRadius: 30,
                overflow: 'hidden',
              }}
            />
          </View>

          <View
            style={{
              flex: 1,
              height: 2,
              backgroundColor: '#f0c420',
            }}
          />
        </View>

        <View
          style={{
            width: '100%',
            padding: 10,
            borderRadius: 15,
          }}
        >
          <View
            style={{
              alignItems: 'center',
              marginBottom: 10,
              flex: 1,
              flexDirection: 'row',
              justifyContent: 'space-between',
            }}
          >
            <Text
              style={{
                color: 'white',
                fontSize: 18,
                padding: 6,
                borderLeftWidth: 2,
                borderBottomWidth: 2,
                borderColor: '#f0c420',
              }}
            >
              Suas Partidas
            </Text>
            <Text
              style={{
                color: 'white',
                fontSize: 14,
                borderColor: '#f0c420',
                borderWidth: 3,
                padding: 6,
                borderRadius: 5,
              }}
            >
              RODADA 1
            </Text>
          </View>
          {/* {matches.map((match) => (
            <View key={match.simulationId} style={[styles.playerCard]}>
              <View
                style={{
                  flexDirection: 'row',
                  width: '100%',
                  alignItems: 'center',
                  justifyContent: 'space-evenly',
                }}
              >
                <View
                  style={{
                    flex: 0.4,
                    flexDirection: 'row',
                    backgroundColor: 'red',
                    alignItems: 'center',
                    justifyContent: 'flex-start',
                  }}
                >
                  {match.win && (
                    <FontAwesome6 name="medal" size={24} color="#C4932F" />
                  )}
                  <Text
                    numberOfLines={1}
                    lineBreakMode="tail"
                    adjustsFontSizeToFit
                    textBreakStrategy="balanced"
                    style={[styles.playerName, { margin: 5, padding: 3 }]}
                  >
                    {match.challengerTeamName}
                  </Text>
                </View>

                <View
                  style={{
                    flex: 0.2,
                    alignItems: 'center',
                    backgroundColor: 'green',
                  }}
                >
                  <MaterialCommunityIcons
                    name="sword-cross"
                    size={30}
                    color="red"
                  />
                </View>

                <View
                  style={{
                    flex: 0.4,
                    alignItems: 'center',
                    flexDirection: 'row',
                    justifyContent: 'flex-end',
                  }}
                >
                  <Text
                    numberOfLines={1}
                    lineBreakMode="tail"
                    adjustsFontSizeToFit
                    textBreakStrategy="balanced"
                    style={[styles.playerName, { margin: 5, padding: 3 }]}
                  >
                    {match.challengedTeamName}
                  </Text>
                  {!match.win && (
                    <FontAwesome6 name="medal" size={24} color="#C4932F" />
                  )}
                </View>
              </View>
            </View>
          ))} */}
          {matches.map((match) => (
            <View
              key={match.simulationId}
              style={[
                styles.playerCard,
                {
                  marginBottom: 15,
                  padding: 5,
                  borderRadius: 10,
                  flexDirection: 'row',
                  alignItems: 'center',
                  justifyContent: 'space-evenly',
                },
              ]}
            >
              <View
                style={{
                  flex: 0.4,
                  flexDirection: 'row',
                  alignItems: 'center',
                  justifyContent: 'flex-end',
                }}
              >
                {match.win && (
                  <FontAwesome6
                    name="medal"
                    size={20}
                    color="white"
                    style={{
                      marginRight: 4,
                    }}
                  />
                )}
                <Text
                  numberOfLines={1}
                  lineBreakMode="tail"
                  adjustsFontSizeToFit
                  textBreakStrategy="balanced"
                  style={[
                    styles.playerName,
                    {
                      marginLeft: 5,
                    },
                  ]}
                >
                  {match.challengerTeamName}
                </Text>
              </View>

              <View
                style={{
                  flex: 0.2,
                  alignItems: 'center',
                  justifyContent: 'center',
                }}
              >
                <MaterialCommunityIcons
                  name="sword-cross"
                  size={26}
                  color="red"
                />
              </View>

              <View
                style={{
                  flex: 0.4,
                  flexDirection: 'row',
                  alignItems: 'center',
                  justifyContent: 'flex-start',
                }}
              >
                <Text
                  numberOfLines={1}
                  lineBreakMode="tail"
                  adjustsFontSizeToFit
                  textBreakStrategy="balanced"
                  style={[styles.playerName, { marginRight: 5 }]}
                >
                  {match.challengedTeamName}
                </Text>
                {!match.win && (
                  <FontAwesome6
                    name="medal"
                    size={20}
                    color="white"
                    style={{ marginLeft: 4 }}
                  />
                )}
              </View>
            </View>
          ))}
        </View>

        <View
          style={{
            flex: 1,
            flexDirection: 'row',
            alignItems: 'center',
          }}
        >
          <View
            style={{
              flex: 1,
              height: 2,
              backgroundColor: '#f0c420',
            }}
          />

          <View
            style={{
              flex: 1,
              alignItems: 'center',
            }}
          >
            <MaterialCommunityIcons name="trophy" size={50} color="#f0c420" />
          </View>

          <View
            style={{
              flex: 1,
              height: 2,
              backgroundColor: '#f0c420',
            }}
          />
        </View>

        <View
          style={{
            width: '100%',
            marginTop: 20,
            padding: 10,
            borderRadius: 15,
          }}
        >
          <Text style={{ color: '#C4932F', fontSize: 28 }}>Melhores Times</Text>
          {teams.map((team, index) => (
            <View
              key={index}
              style={[
                styles.cardTimes,
                {
                  flex: 1,
                  flexDirection: 'row',
                },
              ]}
            >
              {/* <Image source={player.image} style={styles.playerImage} /> */}
              <Text
                style={{
                  color: 'white',
                  fontSize: 16,
                  padding: 5,
                  marginRight: 20,
                }}
              >
                {index + 1}
              </Text>
              <View
                style={{ flexDirection: 'row', flex: 1, alignItems: 'center' }}
              >
                <FontAwesome5 name="user" size={25} color={'white'} />
                <Text style={[styles.playerName, { margin: 5 }]}>
                  {team.name}
                </Text>
              </View>
            </View>
          ))}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  inputWrapper: {
    position: 'relative',
    borderWidth: 2,
    borderColor: '#f0c420',
    borderRadius: 6,
    padding: 10,
  },
  teamLabel: {
    position: 'absolute',
    top: -10,
    left: 0,
    paddingRight: 20,
    paddingLeft: 20,
    fontSize: 20,
    backgroundColor: '#0A131D',
    color: 'white',
  },
  container: {
    paddingVertical: 20,
    paddingHorizontal: 10,
    flexGrow: 1,
    width: '100%',
  },
  memberName: {
    backgroundColor: '#c49b3b',
    color: '#000',
    fontWeight: 'bold',
    flex: 1,
    textAlign: 'center',
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 4,
    fontSize: 11,
  },
  teamName: {
    color: '#fff',
    alignSelf: 'center',
    fontSize: 25,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  playersContainer: {
    marginTop: 20,
    marginBottom: 20,
    flexDirection: 'row',
    justifyContent: 'space-between',
    flexWrap: 'wrap',
  },
  playerCard: {
    alignItems: 'center',
    flexDirection: 'column',
    padding: 5,
    marginHorizontal: 1,
    flex: 1,
  },
  cardTimes: {
    alignItems: 'center',
    flexDirection: 'column',
    margin: 10,
    padding: 5,
  },
  playerImage: {
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: '#222',
  },
  playerName: {
    color: '#fff',
    fontWeight: 'bold',
    marginTop: 10,
  },
  statsContainer: {
    position: 'absolute',
    bottom: -10,
    right: 1,
    paddingRight: 5,
    paddingLeft: 10,
    fontSize: 20,
    backgroundColor: '#0A131D',
    flexDirection: 'row',
  },
  statBox: {
    alignItems: 'center',
    marginHorizontal: 10,
    flexDirection: 'row',
  },
  statValue: {
    fontSize: 20,
    marginLeft: 5,
    color: 'white',
  },
  statLabel: {
    color: 'white',
    fontSize: 12,
  },
  noTeamContainer: {
    alignItems: 'center',
    justifyContent: 'center',
    flex: 1,
  },
  noTeamText: {
    color: '#fff',
    fontSize: 18,
    marginBottom: 20,
  },
  button: {
    backgroundColor: '#6F89FA',
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 8,
  },
  buttonText: {
    color: '#000',
    fontWeight: 'bold',
  },
});

export default HomeScreen;
