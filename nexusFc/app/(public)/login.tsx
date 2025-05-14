// import useForm from '@/hooks/use-form';
// import Yup from '@/utils/yup';
import { isAxiosError } from 'axios';
import React, { FC } from 'react';
import { FormProvider, SubmitHandler } from 'react-hook-form';
import {
  Image,
  SafeAreaView,
  ScrollView,
  Text,
  ToastAndroid,
  TouchableOpacity,
  View,
} from 'react-native';
import { router, Stack } from 'expo-router';
import InputLabel from '@/components/input-label';
import Button from '@/components/button';

const logo = require('../../assets/images/logo.jpg');

// const validationSchema = Yup.object().shape({
//   email: Yup.string().required().email().default(''),
//   password: Yup.string().required().default(''),
// });

const LoginScreen: FC = () => {
  //   const form = useForm({ validationSchema });

  const onSubmit: SubmitHandler<FormValues> = async ({ email, password }) => {
    try {
      // login
      //   const user = await UserService.login({ email, password });
      ToastAndroid.show('Usuário cadastrado', ToastAndroid.LONG);

      //   globalStore.set('user', user);

      router.navigate('/explore');
    } catch (error) {
      console.error(error);

      let message = 'Falha ao realizar o login.';

      if (isAxiosError(error) && error.response?.status === 401) {
        message = 'Email ou senha incorretos.';
      }

      ToastAndroid.show(message, ToastAndroid.LONG);
    }
  };

  const onPressSignup = () => {
    router.navigate('/');
  };

  return (
    <SafeAreaView style={{ flex: 1, backgroundColor: '#0A131D' }}>
      <View
        style={{
          padding: 6,
          justifyContent: 'center',
          alignContent: 'center',
          marginTop: 10,
        }}
      >
        <Image
          style={{ alignSelf: 'center', height: '40%', width: '35%', marginTop: 70 }}
          source={logo}
        />
      </View>
      <ScrollView
        style={{
          padding: 5,
          flex: 1,
        }}
      >
        <Stack.Screen options={{ headerShown: false }} />

        <View
          style={{
            flex: 1,
            justifyContent: 'center',
            // backgroundColor: 'yellow',
          }}
        >
          <View
            style={{
              flex: 1,
              justifyContent: 'center',
              gap: 4,
              paddingHorizontal: 30,
            }}
          >
            <InputLabel
              label="E-mail"
              labelStyle={{
                fontSize: 16,
                padding: 5,
                fontWeight: 'bold',
                marginBottom: 2,
                color: 'white',
              }}
              inputStyle={{
                color: 'white',
                height: 50,
                padding: 10,
                width: '100%',
                backgroundColor: 'white',
                borderRadius: 10,
                marginBottom: 30,
                paddingHorizontal: 10,
              }}
              placeholderTextColor={'black'}
              placeholder="Digite seu email"
              keyboardType="email-address"
              textContentType="emailAddress"
              autoComplete="email"
              autoCapitalize="none"
            />

            <InputLabel
              label="Senha"
              labelStyle={{
                fontSize: 16,
                padding: 5,
                fontWeight: 'bold',
                marginBottom: 2,
                color: 'white',
              }}
              inputStyle={{
                color: 'white',
                height: 50,
                padding: 10,
                width: '100%',
                backgroundColor: 'white',
                borderRadius: 10,
                marginBottom: 30,
                paddingHorizontal: 10,
              }}
              placeholderTextColor={'black'}
              placeholder="Digite sua senha"
              autoComplete="password"
              textContentType="password"
              autoCapitalize="none"
              secureTextEntry
            />

            <Button onPress={onSubmit} title="Entrar" />

            <TouchableOpacity
              style={{
                paddingHorizontal: 3,
                paddingVertical: 10,
                flexDirection: 'row',
                justifyContent: 'center',
              }}
              onPress={onPressSignup}
            >
              <Text style={{ fontSize: 15, color: 'white' }}>
                Novo no NexusFC?{' '}
                <Text
                  style={{
                    fontSize: 16,
                    fontWeight: 'bold',
                    color: '#C4932F',
                  }}
                >
                  Cadastre-se
                </Text>
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

type FormValues = {
  email: string;
  password: string;
};

export default LoginScreen;
