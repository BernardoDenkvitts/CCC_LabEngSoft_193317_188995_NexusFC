import { ObjectId } from '@/utils/types/utils';
import apiRequest from './api-request';

class ProfessionalTeamsService {
  private path = 'professional/teams';

  async get(): Promise<ProfessionalTeam[]> {
    const { data } = await apiRequest.get<ProfessionalTeam[]>(this.path);

    return data?.content;
  }

  async find(id: string | undefined) {
    console.log('aaaaaaaaaaa', id);
    const { data } = await apiRequest.get<ProfessionalTeam[]>(`${this.path}`, {
      params: { id: id },
    });

    console.log('data', data);

    return data?.content.filter((team) => {
      return team.id === id;
    });
  }
}

export type ProfessionalTeam = {
  id: ObjectId;
  league: string;
  name: string;
  region: string;
};

export default new ProfessionalTeamsService();
