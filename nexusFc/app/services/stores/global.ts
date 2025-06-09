import { MMKV } from 'react-native-mmkv';
import StoreBase from './base';
import { User } from '../user';

export type GlobalStorageValues = {
  user: User | undefined;
};

class GlobalStore extends StoreBase<GlobalStorageValues> {
  constructor() {
    super(new MMKV());
  }
}

export default new GlobalStore();
