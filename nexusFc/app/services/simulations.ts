import { ObjectId } from '@/utils/types/utils';
import apiRequest from './api-request';

class SimulationsService {
  private path = 'simulations';

  async get() {
    const { data } = await apiRequest.get<Simulation[]>(this.path);

    return data;
  }

  async find(id: string | undefined) {
    const { data } = await apiRequest.get<Simulation[]>(`${this.path}/${id}`);

    return data;
  }
}

export type Simulation = {
  _id: ObjectId;
  versus_player: boolean;
  desafiante_id: ObjectId;
  desafiado_id: ObjectId;
  status: 'REQUESTED' | 'DENIED' | 'ACCEPTED';
  bet_value: number;
  created_at: string;
  desafiante_team: ObjectId[];
};

export default new SimulationsService();
