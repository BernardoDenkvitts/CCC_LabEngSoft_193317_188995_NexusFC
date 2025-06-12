import { ObjectId } from '@/utils/types/utils';
import apiRequest from './api-request';
import { ProfessionalPlayer } from './professional-players';

class UserService {
  private path_authentication = 'auth';
  private path_user = 'users';

  async create(user: UserCreate) {
    const { data } = await apiRequest.post<User>(
      `${this.path_authentication}/register`,
      {
        ...user,
      },
    );

    return data;
  }

  async update(id: number, user: Partial<UserCreate>) {
    const { data } = await apiRequest.patch<User>(
      `${this.path_authentication}/${id}`,
      {
        user,
      },
    );

    return data;
  }

  async login(user: UserLogin) {
    const { data } = await apiRequest.post<User>(
      `${this.path_authentication}/login`,
      {
        ...user,
      },
    );

    return data;
  }

  async getTeam(id: string | undefined): Promise<UserTeam> {
    const { data } = await apiRequest.get<UserTeam>(
      `${this.path_user}/${id}/team`,
    );

    return data;
  }

  async updateTeamName(
    id: string | undefined,
    newTeamName: string,
  ): Promise<UserTeam> {
    const { data } = await apiRequest.patch<UserTeam>(
      `${this.path_user}/${id}/team?newTeamName=${encodeURIComponent(newTeamName)}`,
    );

    return data;
  }
}

export type UserCreate = {
  name: string;
  email: string;
  password: string;
};

export type User = {
  _id: ObjectId;
  name: string;
  email: string;
  password: string;
  created_at: string;
  last_rewarded_login: string;
  coins: number;
  token: string;
};

type ProfessionalPlayerEntry = {
  player: ProfessionalPlayer;
  isStarter: boolean;
};

export type UserTeam = {
  _id: ObjectId;
  name: string;
  user_id: ObjectId;
  professionalPlayers: ProfessionalPlayerEntry[];
};

export type UserLogin = {
  email: string;
  password: string;
};

export default new UserService();
