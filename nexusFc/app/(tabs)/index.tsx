import FormInput from '@/components/form-input';
import useForm from '@/utils/use-form';
import Yup from '@/utils/yup';
import { FontAwesome, FontAwesome5 } from '@expo/vector-icons';
import React from 'react';
import { View, Text, Image, StyleSheet, ScrollView } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

const mockTeam = {
  name: 'ChurrascadosTeam',
  players: [
    { name: 'Robo', image: '' },
    { name: 'Shini', image: '' },
    { name: 'Mago', image: '' },
    { name: 'Marvin', image: '' },
    { name: 'Kuri', image: '' },
  ],
  patrimonio: 49.2,
  pontos: 425.08,
};

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

const logoLPL = require('../../assets/images/lpl-logo.png');

const HomeScreen = () => {
  const team = mockTeam; // ou `null` se não houver time criado

  return (
    <SafeAreaView
      edges={['bottom', 'left', 'right', 'top']}
      style={{ flex: 1, backgroundColor: '#0A131D' }}
    >
      <ScrollView contentContainerStyle={styles.container}>
        <View
          style={{
            paddingHorizontal: 10,
            paddingVertical: 20,
            borderRadius: 15,
            backgroundColor: '#0D577C',
          }}
        >
          <Text style={styles.teamName}>{team.name}</Text>
          <View style={styles.playersContainer}>
            {team.players.map((player, index) => (
              <View key={index} style={styles.playerCard}>
                <FontAwesome5 name="user" size={40} color={'white'} />
                {/* <Image source={player.image} style={styles.playerImage} /> */}
                <Text style={styles.playerName}>{player.name}</Text>
              </View>
            ))}
          </View>
          <View style={styles.statsContainer}>
            <View style={styles.statBox}>
              <Text style={styles.statValue}>{team.patrimonio}</Text>
              <Text style={styles.statLabel}>PATRIMÔNIO</Text>
            </View>
            <View style={styles.statBox}>
              <Text style={styles.statValue}>{team.pontos}</Text>
              <Text style={styles.statLabel}>PONTOS TOTAIS</Text>
            </View>
          </View>
        </View>

        <View
          style={{
            marginVertical: 20,
            width: '100%',
            alignItems: 'center',
          }}
        >
          <Image source={logoLPL} style={{ width: '75%', height: 40 }} />
        </View>

        <View
          style={{
            width: '100%',
            backgroundColor: '#0D577C',
            padding: 10,
            borderRadius: 15,
          }}
        >
          <Text style={{ color: '#C4932F', fontSize: 28 }}>Suas partidas</Text>
          {matchs.map((match, index) => (
            <View key={index} style={[styles.playerCard, { flex: 1 }]}>
              {/* <Image source={player.image} style={styles.playerImage} /> */}
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
                    flex: 1,
                    flexDirection: 'row',
                    justifyContent: 'flex-start',
                  }}
                >
                  <FontAwesome5 name="user" size={25} color={'white'} />
                  <Text style={[styles.playerName, { margin: 5 }]}>
                    {match.team1}
                  </Text>
                </View>

                <FontAwesome color="#C4932F" name="close" size={25} />

                <View
                  style={{
                    flex: 1,
                    flexDirection: 'row',
                    justifyContent: 'flex-end',
                  }}
                >
                  <FontAwesome5 name="user" size={25} color={'white'} />
                  <Text style={[styles.playerName, { margin: 5 }]}>
                    {match.team2}
                  </Text>
                </View>
              </View>
            </View>
          ))}
        </View>

        <View
          style={{
            width: '100%',
            marginTop: 20,
            backgroundColor: '#0D577C',
            padding: 10,
            borderRadius: 15,
          }}
        >
          <Text style={{ color: '#C4932F', fontSize: 28 }}>Melhores Times</Text>
          {teams.map((team, index) => (
            <View
              key={index}
              style={[
                styles.playerCard,
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
  container: {
    padding: 20,
    backgroundColor: '#111',
    flexGrow: 1,
    width: '100%',
  },
  teamName: {
    color: '#fff',
    alignSelf: 'center',
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
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginTop: 30,
    width: '100%',
  },
  statBox: {
    alignItems: 'center',
  },
  statValue: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#C4932F',
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
