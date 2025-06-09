import { GlobalStore } from '@/services/stores';
import useStoreValue from './use-store-value';

const useCurrentUser = () => useStoreValue(GlobalStore, 'user');

export default useCurrentUser;
