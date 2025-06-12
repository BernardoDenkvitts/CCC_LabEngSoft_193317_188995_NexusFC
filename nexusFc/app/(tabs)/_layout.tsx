import { router, Tabs } from 'expo-router';
import React from 'react';
import { Platform } from 'react-native';
import { HapticTab } from '@/components/HapticTab';
import TabBarBackground from '@/components/ui/TabBarBackground';
import { Colors } from '@/constants/Colors';
import { useColorScheme } from '@/hooks/useColorScheme';
import {
  FontAwesome5,
  MaterialIcons,
  SimpleLineIcons,
} from '@expo/vector-icons';

export default function TabLayout() {
  const colorScheme = useColorScheme();

  return (
    <Tabs
      screenOptions={{
        tabBarActiveTintColor: Colors[colorScheme ?? 'light'].tint,
        headerShown: false,
        tabBarButton: HapticTab,
        tabBarBackground: TabBarBackground,
        tabBarStyle: Platform.select({
          ios: {
            position: 'absolute',
          },
          default: {},
        }),
      }}
    >
      <Tabs.Screen
        name="index"
        options={{
          title: 'Home',
          tabBarIcon: ({ color }) => (
            <FontAwesome5 size={26} name="home" color={color} />
          ),
        }}
      />
      <Tabs.Screen
        name="simulacao"
        options={{
          title: 'Simulações',
          tabBarIcon: ({ color }) => (
            <SimpleLineIcons name="game-controller" size={24} color={color} />
          ),
        }}
      />
      <Tabs.Screen
        name="escalacao"
        options={{
          title: 'Escalação',
          tabBarIcon: ({ color }) => (
            <MaterialIcons size={28} name="groups" color={color} />
          ),
        }}
      />
      <Tabs.Screen
        name="logout"
        options={{
          title: 'Sair',
          tabBarIcon: ({ color }) => (
            <MaterialIcons name="logout" size={24} color={color} />
          ),
        }}
        listeners={() => ({
          tabPress: (e) => {
            e.preventDefault();
            router.replace('/(public)/login');
          },
        })}
      />
    </Tabs>
  );
}
