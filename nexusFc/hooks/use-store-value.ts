import { useEffect, useState } from 'react';
import useImmutableCallback from './use-imutable-callback';
import StoreBase from '@/services/stores/base';

const useStoreValue = <
  TStore extends StoreBase<any>,
  TKey extends keyof ExtractStoreValues<TStore>,
>(
  store: TStore,
  key: TKey,
) => {
  type StoreValue = ExtractStoreValues<TStore>[TKey];

  const [value, setValue] = useState<StoreValue>(() => store.get(key));

  useEffect(() => {
    const subscription = store.addOnChange((keyChanged) => {
      if (keyChanged === key) {
        setValue(store.get(key));
      }
    });

    return () => subscription.remove();
  }, [store, key]);

  const setStoreValue = useImmutableCallback((value: StoreValue) => {
    store.set(key, value);
  });

  return [value, setStoreValue] as const;
};

type ExtractStoreValues<T> = T extends StoreBase<infer V> ? V : never;

export default useStoreValue;
