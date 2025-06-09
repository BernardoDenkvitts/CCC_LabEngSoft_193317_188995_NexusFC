import { useEffect, useMemo, useState } from 'react';
import { isFunction } from 'lodash';
import useImmutableCallback from './use-imutable-callback';
import { ToastAndroid } from 'react-native';

const useAsyncFetch = <T>(
  { callback, errorMessage, onError }: Params<T>,
  dependencies: any[] = [],
) => {
  const [data, setData] = useState<T | undefined>(undefined);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<unknown>();

  const fetch = useImmutableCallback(
    async (isAborted: () => boolean = () => false) => {
      try {
        setData(undefined);
        setLoading(true);
        setError(undefined);

        const newData = await callback();

        if (!isAborted()) setData(newData);
      } catch (fetchError) {
        console.error(fetchError);

        if (!isAborted()) setError(fetchError);
      } finally {
        if (!isAborted()) setLoading(false);
      }
    },
  );

  useEffect(() => {
    if (!error) return;

    if (onError) onError(error);

    const message = isFunction(errorMessage)
      ? errorMessage(error)
      : errorMessage;

    if (message) {
      ToastAndroid.show(message, ToastAndroid.LONG);
    }
  }, [error, errorMessage, onError]);

  useEffect(() => {
    let aborted = false;

    fetch(() => aborted);

    return () => {
      aborted = true;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, dependencies);

  const refetch = useImmutableCallback(() => fetch());

  return useMemo(
    () => ({ loading, data, error, refetch }),
    [loading, data, error, refetch],
  );
};

type Params<T> = {
  callback: () => Promise<T> | undefined;
  onError?: (error: unknown) => void;
  errorMessage?: string | ((error: unknown) => string | undefined);
};

export default useAsyncFetch;
