import apiRequest from './api-request';

class UserService {
  private path = 'auth';

  async create(user: UserCreate) {
    console.log('create user', user);
    const { data } = await apiRequest.post<any>(`${this.path}/register`, {
      ...user,
    });

    return data;
  }

  async update(id: number, user: Partial<UserCreate>) {
    const { data } = await apiRequest.patch<any>(`${this.path}/${id}`, {
      user,
    });

    return data;
  }

  async login(user: UserLogin) {
    const { data } = await apiRequest.post<any>(`${this.path}/login`, {
      ...user,
    });

    return data;
  }
}

export type UserCreate = {
  name: string;
  email: string;
  password: string;
};

type UserLogin = {
  email: string;
  password: string;
};

export default new UserService();
