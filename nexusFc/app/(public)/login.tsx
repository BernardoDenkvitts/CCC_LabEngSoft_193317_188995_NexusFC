// import useForm from '@/hooks/use-form';
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
import Button from '@/components/button';
import Yup from '@/utils/yup';
import useForm from '@/utils/use-form';
import FormInput from '@/components/form-input';

const logo = require('../../assets/images/logo.jpg');

const validationSchema = Yup.object().shape({
  email: Yup.string().required().email().default(''),
  password: Yup.string().required().default(''),
});

const LoginScreen: FC = () => {
  const form = useForm({ validationSchema });

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
          justifyContent: 'center',
          alignContent: 'center',
        }}
      >
        <Image
          style={{
            alignSelf: 'center',
            height: '40%',
            width: '35%',
            marginTop: 70,
          }}
          source={logo}
        />
      </View>
      <ScrollView
        style={{
          paddingHorizontal: 5,
          flex: 1,
        }}
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
                label="E-mail"
                placeholder="Digite seu email"
                name="email"
                keyboardType="email-address"
                textContentType="emailAddress"
                autoComplete="email"
                autoCapitalize="none"
              />

              <FormInput
                containerStyle={{ marginVertical: 10 }}
                labelStyle={{ color: 'white', marginBottom: 5 }}
                label="Senha"
                placeholder="Digite sua senha"
                name="password"
                autoComplete="password"
                textContentType="password"
                autoCapitalize="none"
                secureTextEntry
              />

              <Button
                containerStyle={{
                  marginTop: 10,
                }}
                onPress={form.handleSubmit(onSubmit)}
                loading={form.formState.isSubmitting}
                title="Entrar"
              />

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
          </FormProvider>
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
