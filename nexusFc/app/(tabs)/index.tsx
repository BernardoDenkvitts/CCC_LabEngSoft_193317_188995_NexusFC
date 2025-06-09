import useAsyncFetch from '@/hooks/use-async-fetch';
import useCurrentUser from '@/hooks/use-current-user';
import {
  FontAwesome,
  FontAwesome5,
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
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import UserService, { UserTeam } from '@/services/user';
import ProfessionalPlayersService, {
  ProfessionalPlayer,
} from '@/services/professional-players';
import { empty } from '@/constants';

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
  const [currentUser] = useCurrentUser();
  const [userTeam, setUserTeam] = useState<UserTeam | null>(null);
  const [loading, setLoading] = useState(false);

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

  useEffect(() => {
    if (!currentUser?._id) return;

    const fetchTeam = async () => {
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

    fetchTeam();
  }, [currentUser]);

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
              <Text style={styles.teamLabel}>{mockTeam.name}</Text>
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
                          lineBreakMode="middle"
                        >
                          {player.nick}
                        </Text>
                      </View>
                    );
                  })
                ) : (
                  <Text
                    style={{
                      backgroundColor: '#c49b3b',
                      color: 'white',
                      padding: 8,
                      borderRadius: 4,
                      fontSize: 16,
                    }}
                  >
                    Nenhum jogador cadastrado no time.
                  </Text>
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

    backgroundColor: '#white',
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
    fontSize: 10,
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
    // alignSelf: 'flex-end',
    // justifyContent: 'space-around',
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
