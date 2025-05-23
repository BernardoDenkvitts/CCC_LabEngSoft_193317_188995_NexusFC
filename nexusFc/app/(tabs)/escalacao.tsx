import FormInput from '@/components/form-input';
import useForm from '@/utils/use-form';
import Yup from '@/utils/yup';
import { FontAwesome5 } from '@expo/vector-icons';
import { Stack } from 'expo-router';
import React from 'react';
import { FormProvider } from 'react-hook-form';
import { View, StyleSheet, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

const validationSchema = Yup.object().shape({
  teamName: Yup.string().required().default(''),
  sigla: Yup.string().required().default(''),
  players: Yup.object().shape({
    top: Yup.string().required().default(''),
    mid: Yup.string().required().default(''),
    adc: Yup.string().required().default(''),
    supp: Yup.string().required().default(''),
    jg: Yup.string().required().default(''),
  }),
});

const Escalacao = () => {
  const form = useForm({ validationSchema });

  return (
    <SafeAreaView
      edges={['bottom', 'left', 'right', 'top']}
      style={{ flex: 1, backgroundColor: '#0A131D' }}
    >
      <Stack.Screen options={{ headerShown: false }} />

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
              paddingHorizontal: 30,
            }}
          >
            <FormInput
              containerStyle={{ marginVertical: 10 }}
              labelStyle={{ color: 'white', marginBottom: 5 }}
              label="Nome do Time"
              placeholder="ex: Pain Gaming"
              name="teamName"
              keyboardType="ascii-capable"
              textContentType="nickname"
              autoCapitalize="words"
            />

            <FormInput
              containerStyle={{ marginVertical: 10 }}
              labelStyle={{ color: 'white', marginBottom: 5 }}
              label="Sigla do Time"
              placeholder="ex: PNG"
              name="sigla"
              keyboardType="ascii-capable"
              textContentType="nickname"
              autoCapitalize="characters"
            />
          </View>
        </FormProvider>
      </View>
    </SafeAreaView>
  );
};

export default Escalacao;

const styles = StyleSheet.create({
  container: {
    padding: 20,
    backgroundColor: '#111',
    flexGrow: 1,
    alignItems: 'center',
  },
  teamName: {
    color: '#fff',
    fontSize: 22,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  playersContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    flexWrap: 'wrap',
  },
  playerCard: {
    alignItems: 'center',
    margin: 10,
  },
  playerImage: {
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: '#222',
  },
  playerName: {
    color: '#fff',
    marginTop: 5,
  },
  statsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginTop: 30,
    width: '100%',
  },
  statBox: {
    alignItems: 'center',
  },
  statValue: {
    color: '#fff',
    fontSize: 20,
    fontWeight: 'bold',
  },
  statLabel: {
    color: '#aaa',
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
