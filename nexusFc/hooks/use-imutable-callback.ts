import { useCallback, useRef } from 'react';

const useImmutableCallback = <T extends (...args: any[]) => any>(
  callback: T,
) => {
  const callbackRef = useRef(callback);
  callbackRef.current = callback;

  return useCallback(
    (...args: Parameters<T>): ReturnType<T> => callbackRef.current(...args),
    [],
  );
};

export default useImmutableCallback;
