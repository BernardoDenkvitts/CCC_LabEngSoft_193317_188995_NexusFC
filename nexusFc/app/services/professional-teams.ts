import { ObjectId } from '@/utils/types/utils';
import apiRequest from './api-request';

class ProfessionalTeamsService {
  private path = 'professional/teams';

  async get() {
    const { data } = await apiRequest.get<ProfessionalTeam[]>(this.path);

    return data;
  }

  async find(id: string | undefined) {
    // const formatedFilter = deepSnakeCase(filters);

    const { data } = await apiRequest.get<ProfessionalTeam[]>(
      `${this.path}/${id}`,
    );

    return data;
  }
}

export type ProfessionalTeam = {
  id: ObjectId;
  league: string;
  name: string;
  region: string;
};

export default new ProfessionalTeamsService();
