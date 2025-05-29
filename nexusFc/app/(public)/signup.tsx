import React, { FC } from 'react';
import { FormProvider, SubmitHandler } from 'react-hook-form';
import {
  Image,
  SafeAreaView,
  ScrollView,
  ToastAndroid,
  View,
} from 'react-native';
import UserService, { UserCreate } from '@/services/user';
import { router, Stack } from 'expo-router';
import Button from '@/components/button';
import Yup from '@/utils/yup';
import useForm from '@/utils/use-form';
import FormInput from '@/components/form-input';
import { GlobalStore } from '@/services/stores';

const logo = require('../../assets/images/logo.jpg');

const validationSchema = Yup.object().shape({
  name: Yup.string().required().default(''),
  email: Yup.string().required().email().default(''),
  password: Yup.string().required().default(''),
});

const SignupScreen: FC = () => {
  const form = useForm({ validationSchema });

  const onSubmit: SubmitHandler<UserCreate> = async ({
    name,
    email,
    password,
  }) => {
    try {
      const user = await UserService.create({ name, email, password });

      GlobalStore.set('user', user);

      ToastAndroid.show('Usuário registrado', ToastAndroid.LONG);

      router.navigate('/explore');
    } catch (error) {
      console.error(error);

      let message = 'Falha ao se registrar.';

      ToastAndroid.show(message, ToastAndroid.LONG);
    }
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
                label="Nome"
                placeholder="Digite seu nome"
                name="name"
                keyboardType="default"
                textContentType="name"
                autoComplete="name"
                autoCapitalize="words"
              />
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
                title="Registrar-se"
              />
            </View>
          </FormProvider>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

export default SignupScreen;
