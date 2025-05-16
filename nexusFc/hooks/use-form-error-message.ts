import { isString } from 'lodash';
import { useMemo } from 'react';
import { FieldValues, FormState, get } from 'react-hook-form';

const useFormErrorMessage = <T extends FieldValues>(
  formState: FormState<T>,
  name: Extract<keyof T, string>,
) =>
  useMemo(() => {
    const isTouched = get(formState.touchedFields, name);
    const message = get(formState.errors, name)?.message;

    return isString(message) && (isTouched || formState.isSubmitted)
      ? message
      : ' ';
  }, [formState, name]);

export default useFormErrorMessage;
