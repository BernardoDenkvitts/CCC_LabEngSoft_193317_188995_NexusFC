import { MMKV } from 'react-native-mmkv';
import StoreBase from './base';
import { UserWithToken } from '@/utils/types/user';

export type GlobalStorageValues = {
  user: UserWithToken | undefined;
};

class GlobalStore extends StoreBase<GlobalStorageValues> {
  constructor() {
    super(new MMKV());
  }
}

export default new GlobalStore();
