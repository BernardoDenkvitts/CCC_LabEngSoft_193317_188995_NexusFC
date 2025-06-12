import axios from 'axios';
import ApiBase from './api-base';
import { GlobalStore } from './stores';

class ApiRequest extends ApiBase {
  url = 'http://192.168.0.49:8080/';

  constructor() {
    super(
      axios.create({
        baseURL: 'http://192.168.0.49:8080/',
      }),
    );

    this.axios.interceptors.response.use((response) => {
      return response;
    });

    this.axios.interceptors.request.use((request) => {
      const token = GlobalStore.get('user')?.token;

      if (token) {
        request.headers.Authorization = `Bearer ${token}`;
      }

      request.headers['Content-Type'] = 'application/json';

      return request;
    });
  }
}

export default new ApiRequest();
