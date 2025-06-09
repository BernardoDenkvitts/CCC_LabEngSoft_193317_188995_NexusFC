import apiRequest from './api-request';

class TransactionsService {
  private path = 'auth';

  async get() {
    const { data } = await apiRequest.get<Transaction[]>(this.path);

    return data;
  }

  async find(id: string | undefined) {
    // const formatedFilter = deepSnakeCase(filters);

    const { data } = await apiRequest.get<Transaction[]>(`${this.path}/${id}`);

    return data;
  }
}

export type Transaction = {
  id: string;
  league: string;
  name: string;
  region: string;
};

export default new TransactionsService();
