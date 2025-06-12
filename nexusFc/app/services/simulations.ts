import { ObjectId } from '@/utils/types/utils';
import apiRequest from './api-request';

class SimulationsService {
  private path = 'simulations';

  async get() {
    const { data } = await apiRequest.get<Simulation[]>(this.path);

    return data;
  }

  async post({
    challengerId,
    challengedId,
    versusPlayer,
    betValue,
  }: {
    challengerId: string | undefined;
    challengedId: string | undefined;
    versusPlayer: boolean;
    betValue: number;
  }): Promise<Payload> {
    console.log('challengerId', challengerId);
    console.log('challengedId', challengedId);
    console.log('versusPlayer', versusPlayer);
    console.log('betValue', betValue);

    const { data } = await apiRequest.post<any>(this.path, {
      challengerId,
      challengedId,
      versusPlayer,
      betValue,
    });

    return data;
  }

  async history(id: string | undefined): Promise<Simulation[]> {
    const { data } = await apiRequest.get<Simulation[]>(
      `${this.path}/history/${id}`,
      { params: { size: 5, page: 0 } },
    );

    return data?.content;
  }

  async find(id: string | undefined, userId: string | undefined) {
    const { data } = await apiRequest.get<Simulation[]>(`${this.path}/${id}`, {
      params: { id, userId },
    });

    return data;
  }
}

type TeamMember = {
  id: string;
  nick: string;
  lane: string;
  imageUrl: string;
};

type Payload = {
  id: string;
  versusPlayer: boolean;
  challengerId: string;
  challengedId: string;
  status: 'REQUESTED' | 'APPROVED' | 'REJECTED';
  betValue: number;
  createdAt: string;
  win: boolean;
  challengerTeam: TeamMember[];
  challengedTeam: TeamMember[];
  remainingCoins: number;
};

export type Simulation = {
  status: 'REQUESTED' | 'DENIED' | 'ACCEPTED';
  betValue: number;
  challengedId: ObjectId;
  challengedTeam: ObjectId[];
  challengerId: ObjectId;
  challengerTeam: ObjectId[];
  createdAt: string;
  id: ObjectId;
  versusPlayer: boolean;
  win: boolean | null;
};

export default new SimulationsService();
