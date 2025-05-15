import React, { FC } from 'react';
import { Controller, useFormContext } from 'react-hook-form';
// import useFormErrorMessage from '../../hooks/use-form-error-message';
import Input, { InputPropTypes } from '../input';
import { toString } from 'lodash';

const FormInput: FC<FormInputPropTypes> = ({
  name,
  parserOnChange,
  ...inputProps
}) => {
  const { control, formState } = useFormContext();

  //   const errorMessage = useFormErrorMessage(formState, name);

  return (
    <Controller
      control={control}
      name={name}
      render={({ field: { onChange, onBlur, value } }) => (
        <Input
          placeholder={inputProps.label}
          {...inputProps}
          onBlur={onBlur}
          onChangeText={(newValue: string) =>
            onChange(
              parserOnChange ? parserOnChange(newValue, value) : newValue,
            )
          }
          value={toString(value)}
        />
      )}
    />
  );
};

type FormInputPropTypes = Omit<
  InputPropTypes,
  'value' | 'onChange' | 'onBlur' | 'errorMessage'
> & {
  name: string;
  parserOnChange?: (newValue: string, currentValue: any) => any;
  required?: boolean;
};

export default FormInput;
