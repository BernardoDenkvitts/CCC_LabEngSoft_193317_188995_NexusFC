import apiRequest from './api-request';

class MarketsService {
  private path = 'market';

  async buy(
    player_id: string | undefined,
    user_id: string | undefined,
  ): Promise<Market[]> {
    const { data } = await apiRequest.post<Market[]>(`${this.path}/buy`, {
      userId: user_id,
      playerId: player_id,
    });

    console.log('data', data);
    return data;
  }

  async sell(player_id: string | undefined, user_id: string | undefined) {
    const { data } = await apiRequest.post<Market[]>(`${this.path}/sell`, {
      userId: user_id,
      playerId: player_id,
    });

    return data;
  }
}

export type Market = {};

export default new MarketsService();
