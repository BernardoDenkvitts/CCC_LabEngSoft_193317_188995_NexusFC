import { ObjectId } from '@/utils/types/utils';
import apiRequest from './api-request';

class SimulationsService {
  private path = 'simulations';

  async get() {
    const { data } = await apiRequest.get<Simulation[]>(this.path);

    return data;
  }

  async history(id: string | undefined): Promise<Simulation[]> {
    const { data } = await apiRequest.get<Simulation[]>(
      `${this.path}/history/${id}`,
      { params: { size: 5, page: 0 } },
    );

    return data?.content;
  }

  async find(id: string | undefined) {
    const { data } = await apiRequest.get<Simulation[]>(`${this.path}/${id}`);

    return data;
  }
}

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
