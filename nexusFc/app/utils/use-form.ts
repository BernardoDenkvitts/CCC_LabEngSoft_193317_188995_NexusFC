import Yup from '@/utils/yup';
import { useMemo } from 'react';
import { yupResolver } from '@hookform/resolvers/yup';
import {
  Resolver,
  SubmitHandler,
  // eslint-disable-next-line @typescript-eslint/no-restricted-imports
  useForm as useOriginalForm,
  UseFormProps as useOriginalFormProps,
} from 'react-hook-form';
import { FormValuesFromYupSchema } from './types/utils';

const useForm = <TSchema extends Yup.ObjectSchema<any>, TContext = any>({
  validationSchema,
  ...propsRest
}: UseFormProps<TSchema, TContext>) => {
  const defaultValues = useMemo(
    () => propsRest.defaultValues || validationSchema.getDefault(),
    [propsRest.defaultValues, validationSchema],
  );

  type TFieldValues = FormValuesFromYupSchema<TSchema>;

  const resolver = useMemo(
    () =>
      yupResolver(validationSchema) as unknown as Resolver<
        TFieldValues,
        TContext
      >,
    [validationSchema],
  );

  return useOriginalForm<TFieldValues, TContext, TSchema['__outputType']>({
    ...propsRest,
    resolver,
    defaultValues,
    mode: 'onChange',
  });
};

type UseFormProps<TSchema extends Yup.ObjectSchema<any>, TContext = any> = Omit<
  useOriginalFormProps<FormValuesFromYupSchema<TSchema>, TContext>,
  'resolver'
> & {
  validationSchema: TSchema;
};

export type BenbeySubmitHandler<TSchema extends Yup.ObjectSchema<any>> =
  SubmitHandler<TSchema['__outputType']>;

export default useForm;
