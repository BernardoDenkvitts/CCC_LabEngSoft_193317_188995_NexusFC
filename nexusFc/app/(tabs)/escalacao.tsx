import CustomAutocompleteDropdown from '@/components/auto-complete-dropdown';
import Button from '@/components/button';
import FormInput from '@/components/form-input';
import { empty } from '@/constants';
import useAsyncFetch from '@/hooks/use-async-fetch';
import useCurrentUser from '@/hooks/use-current-user';
import market from '@/services/market';
import professionalPlayers from '@/services/professional-players';
import user from '@/services/user';
import useForm from '@/utils/use-form';
import Yup from '@/utils/yup';
import { FontAwesome5 } from '@expo/vector-icons';
import { Stack } from 'expo-router';
import { sumBy } from 'lodash';
import React, { useCallback, useEffect, useMemo } from 'react';
import { FormProvider } from 'react-hook-form';
import { ScrollView, ToastAndroid, TouchableOpacity } from 'react-native';
import { View, StyleSheet, Text, Image } from 'react-native';
import {
  AutocompleteDropdown,
  AutocompleteDropdownContextProvider,
  AutocompleteDropdownItem,
} from 'react-native-autocomplete-dropdown';
import { SafeAreaView } from 'react-native-safe-area-context';

const validationSchema = Yup.object().shape({
  teamName: Yup.string().required().default(''),
  players: Yup.object().shape({
    top: Yup.object()
      .shape({
        id: Yup.string().required('ID é obrigatório'),
        coins: Yup.number()
          .required('Coins é obrigatório')
          .min(0, 'Coins deve ser maior ou igual a 0'),
      })
      .required('Top é obrigatório')
      .default({ id: '', coins: 0 }),

    mid: Yup.object()
      .shape({
        id: Yup.string().required('ID é obrigatório'),
        coins: Yup.number()
          .required('Coins é obrigatório')
          .min(0, 'Coins deve ser maior ou igual a 0'),
      })
      .required('Mid é obrigatório')
      .default({ id: '', coins: 0 }),

    adc: Yup.object()
      .shape({
        id: Yup.string().required('ID é obrigatório'),
        coins: Yup.number()
          .required('Coins é obrigatório')
          .min(0, 'Coins deve ser maior ou igual a 0'),
      })
      .required('ADC é obrigatório')
      .default({ id: '', coins: 0 }),

    supp: Yup.object()
      .shape({
        id: Yup.string().required('ID é obrigatório'),
        coins: Yup.number()
          .required('Coins é obrigatório')
          .min(0, 'Coins deve ser maior ou igual a 0'),
      })
      .required('Support é obrigatório')
      .default({ id: '', coins: 0 }),

    jg: Yup.object()
      .shape({
        id: Yup.string().required('ID é obrigatório'),
        coins: Yup.number()
          .required('Coins é obrigatório')
          .min(0, 'Coins deve ser maior ou igual a 0'),
      })
      .required('Jungle é obrigatório')
      .default({ id: '', coins: 0 }),
  }),
});

const renderItem = (item: ItemDropdownType) => {
  return (
    <View
      style={{
        flexDirection: 'row',
        justifyContent: 'space-between',
        padding: 8,
        backgroundColor: 'white',
        borderBottomWidth: 1,
        borderBottomColor: '#eee',
      }}
    >
      <Text style={{ color: 'black' }}>{item.title}</Text>
      <View
        style={{
          flexDirection: 'row',
          alignItems: 'center',
          justifyContent: 'space-around',
        }}
      >
        <Text
          style={{
            fontWeight: 'bold',
            color: 'black',
            fontStyle: 'italic',
          }}
        >
          {item.cost}
        </Text>
        <FontAwesome5
          name="coins"
          size={24}
          color="#c49b3b"
          style={{ paddingHorizontal: 10 }}
        />
      </View>
    </View>
  );
};

const Escalacao = () => {
  const [currentUser] = useCurrentUser();
  const form = useForm({ validationSchema });
  const players = form.watch('players');

  const { data: topLanersList = empty.array, loading: loadingTop } =
    useAsyncFetch(
      {
        callback: async () => {
          if (!currentUser) return;

          const getPlayers = await professionalPlayers.get('TOP');

          return getPlayers.map<ItemDropdownType>((player) => ({
            id: player.id,
            title: player.nick,
            cost: player.cost,
          }));
        },
        errorMessage: 'Falha ao carregar a lista de top laners.',
      },
      [currentUser],
    );

  const { data: midLanersList = empty.array, loading: loadingMid } =
    useAsyncFetch(
      {
        callback: async () => {
          if (!currentUser) return;

          const getPlayers = await professionalPlayers.get('MID');

          return getPlayers.map<ItemDropdownType>((player) => ({
            id: player.id,
            title: player.nick,
            cost: player.cost,
          }));
        },
        errorMessage: 'Falha ao carregar a lista de mid laners.',
      },
      [currentUser],
    );

  const { data: junglersList = empty.array, loading: loadingJunglers } =
    useAsyncFetch(
      {
        callback: async () => {
          if (!currentUser) return;

          const getPlayers = await professionalPlayers.get('JUNGLE');

          return getPlayers.map<ItemDropdownType>((player) => ({
            id: player.id,
            title: player.nick,
            cost: player.cost,
          }));
        },
        errorMessage: 'Falha ao carregar o histórico de partidas.',
      },
      [currentUser],
    );

  const { data: adCarryList = empty.array, loading: loadingAdc } =
    useAsyncFetch(
      {
        callback: async () => {
          if (!currentUser) return;

          const getPlayers = await professionalPlayers.get('ADC');

          return getPlayers.map<ItemDropdownType>((player) => ({
            id: player.id,
            title: player.nick,
            cost: player.cost,
          }));
        },
        errorMessage: 'Falha ao carregar o histórico de partidas.',
      },
      [currentUser],
    );

  const { data: supList = empty.array, loading: loadingSup } = useAsyncFetch(
    {
      callback: async () => {
        if (!currentUser) return;

        const getPlayers = await professionalPlayers.get('SUP');

        return getPlayers.map<ItemDropdownType>((player) => ({
          id: player.id,
          title: player.nick,
          cost: player.cost,
        }));
      },
      errorMessage: 'Falha ao carregar o histórico de partidas.',
    },
    [currentUser],
  );

  const onSubmit = useCallback(async () => {
    const values = form.getValues();

    try {
      const buyPlayer = async (playerId: string) => {
        return await market.buy(playerId, currentUser?._id);
      };

      const promises = Object.values(values.players).map((player) =>
        player.id ? buyPlayer(player.id) : Promise.resolve(),
      );

      // await Promise.all(promises);

      await user.updateTeamName(currentUser?._id, values.teamName);

      ToastAndroid.show('Time criado com sucesso!', ToastAndroid.CENTER);
    } catch (error) {
      console.error('Error buying players:', error);
      ToastAndroid.show(
        'Aconteceu um erro ao tentar criar o time',
        ToastAndroid.CENTER,
      );
    }
  }, [form, currentUser]);

  const patrimonio = useMemo(() => {
    console.log('players', players);
    const hasAtLeastOneValue = Object.values(players).some((value) => !!value);

    if (!hasAtLeastOneValue) return;

    return Object.values(players).reduce((total, player) => {
      return total + (player.coins || 0);
    }, 0);
  }, [players.adc, players.jg, players.top, players.mid, players.supp]);

  return (
    <AutocompleteDropdownContextProvider headerOffset={-6}>
      <SafeAreaView
        edges={['bottom', 'left', 'right', 'top']}
        style={{ flex: 1, backgroundColor: '#0A131D' }}
      >
        <Stack.Screen
          options={{
            headerShown: true,
            headerTitle: '',
            headerStyle: {
              backgroundColor: '#0A131D',
            },
            headerRight: () => (
              <TouchableOpacity
                onPress={() => onSubmit()}
                style={{
                  marginRight: 15,
                  paddingVertical: 10,
                  paddingHorizontal: 15,
                  backgroundColor: '#c49b3b',
                  borderRadius: 10,
                }}
              >
                <Text style={{ color: 'white', fontWeight: 'bold' }}>
                  Salvar
                </Text>
              </TouchableOpacity>
            ),
          }}
        />

        <ScrollView
          contentContainerStyle={{
            width: '100%',
          }}
        >
          <View
            style={{
              flex: 1,
              justifyContent: 'center',
            }}
          >
            <FormProvider {...form}>
              <View
                style={{
                  flex: 1,
                  justifyContent: 'center',
                  gap: 4,
                  padding: 30,
                  paddingTop: 10,
                }}
              >
                <View style={{}}>
                  <Text
                    style={{
                      color: '#c49b3b',
                      fontSize: 28,
                      textAlign: 'center',
                      padding: 10,
                    }}
                  >
                    Monte seu time
                  </Text>
                  {players && (
                    <View
                      style={{
                        flexDirection: 'row',
                        alignItems: 'center',
                        padding: 5,
                        justifyContent: 'center',
                      }}
                    >
                      <Text
                        style={{
                          fontSize: 16,
                          color: 'grey',
                        }}
                      >
                        PATRIMÔNIO - {patrimonio}
                      </Text>
                      <FontAwesome5
                        name="coins"
                        size={24}
                        color="#c49b3b"
                        style={{ paddingHorizontal: 8 }}
                      />
                    </View>
                  )}
                </View>
                <FormInput
                  containerStyle={{ marginVertical: 10 }}
                  labelStyle={{ color: 'white', marginBottom: 5 }}
                  label="Nome:"
                  required
                  placeholder="ex: Pain Gaming"
                  name="teamName"
                  keyboardType="ascii-capable"
                  textContentType="nickname"
                  autoCapitalize="words"
                />
                <View style={{ maxHeight: 100 }}>
                  <CustomAutocompleteDropdown
                    required
                    label="Top laner:"
                    placeholder="Top laner"
                    renderItem={renderItem}
                    dataSet={topLanersList}
                    loading={loadingTop}
                    onClear={() => {
                      form.setValue('players.top', {
                        id: '',
                        coins: 0,
                      });
                    }}
                    onSelectItem={(item) => {
                      console.log('item', item);
                      if (item) {
                        form.setValue('players.top', {
                          id: item.id,
                          coins: item.cost,
                        });
                      }
                    }}
                  />
                </View>

                <View style={{ maxHeight: 100 }}>
                  <CustomAutocompleteDropdown
                    required
                    label="Jungle:"
                    renderItem={renderItem}
                    placeholder="Jungle"
                    onClear={() => {
                      form.setValue('players.jg', {
                        id: '',
                        coins: 0,
                      });
                    }}
                    dataSet={junglersList}
                    loading={loadingJunglers}
                    onSelectItem={(item) => {
                      if (item) {
                        form.setValue('players.jg', {
                          id: item.id,
                          coins: item.cost,
                        });
                      }
                    }}
                  />
                </View>

                <View style={{ maxHeight: 100 }}>
                  <CustomAutocompleteDropdown
                    required
                    label="Mid laner:"
                    placeholder="Mid laner"
                    onClear={() => {
                      form.setValue('players.mid', {
                        id: '',
                        coins: 0,
                      });
                    }}
                    renderItem={renderItem}
                    dataSet={midLanersList}
                    loading={loadingMid}
                    onSelectItem={(item) => {
                      if (item) {
                        form.setValue('players.mid', {
                          id: item.id,
                          coins: item.cost,
                        });
                      }
                    }}
                  />
                </View>

                <View style={{ maxHeight: 100 }}>
                  <CustomAutocompleteDropdown
                    required
                    label="Adcarry:"
                    placeholder="Adcarry"
                    onClear={() => {
                      form.setValue('players.adc', {
                        id: '',
                        coins: 0,
                      });
                    }}
                    renderItem={renderItem}
                    dataSet={adCarryList}
                    loading={loadingAdc}
                    onSelectItem={(item) => {
                      if (item) {
                        form.setValue('players.adc', {
                          id: item.id,
                          coins: item.cost,
                        });
                      }
                    }}
                  />
                </View>

                <View style={{ maxHeight: 100 }}>
                  <CustomAutocompleteDropdown
                    required
                    label="Support:"
                    direction="up"
                    placeholder="Support"
                    onClear={() => {
                      form.setValue('players.supp', {
                        id: '',
                        coins: 0,
                      });
                    }}
                    renderItem={renderItem}
                    dataSet={supList}
                    loading={loadingSup}
                    onSelectItem={(item) => {
                      if (item) {
                        form.setValue('players.supp', {
                          id: item.id,
                          coins: item.cost,
                        });
                      }
                    }}
                  />
                </View>
              </View>
            </FormProvider>
          </View>
        </ScrollView>
      </SafeAreaView>
    </AutocompleteDropdownContextProvider>
  );
};

export default Escalacao;

type ItemDropdownType = AutocompleteDropdownItem & { cost: number };
