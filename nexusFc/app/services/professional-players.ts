import { ObjectId } from '@/utils/types/utils';
import apiRequest from './api-request';

class ProfessionalPlayersService {
  private path = 'professional/players';

  async get() {
    const { data } = await apiRequest.get<ProfessionalPlayer[]>(this.path);

    return data;
  }

  async find(id: string | undefined) {
    // const formatedFilter = deepSnakeCase(filters);

    const { data } = await apiRequest.get<ProfessionalPlayer[]>(
      `${this.path}/${id}`,
    );

    return data;
  }
}

type MatchHistoryEntry = {
  versus: string;
  champion: string;
  kills: number;
  deaths: number;
  assists: number;
  gold: number;
  cs: number;
  win: boolean;
  vod: string;
  tournament: string;
};

export type ProfessionalPlayer = {
  _id: ObjectId;
  nick: string;
  lane: 'TOP' | 'JUNGLE' | 'MID' | 'ADC' | 'SUPPORT';
  team: ObjectId;
  matchHistory: MatchHistoryEntry[];
  overallKill: number;
  overallDeath: number;
  overallAssist: number;
  overallDamage: number;
  overallGold: number;
  overallCs: number;
  overallWinRate: number;
  cost: number;
  imageUrl: string;
};

export default new ProfessionalPlayersService();
