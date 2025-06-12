import React, { FC } from 'react';
import { Controller, useFormContext } from 'react-hook-form';
import CustomAutocompleteDropdown, {
  CustomAutocompleteDropdownPropTypesPropTypes,
} from '../auto-complete-dropdown';

// TODO: Quebrar a linha na listagem de opçÕes quando o texto form mt grande, ver como lidar com o item selecionado
const FormAutocompleteDropdown: FC<FormInputPropTypes> = ({
  name,
  ...autoCompleteProps
}) => {
  const { control } = useFormContext();

  return (
    <Controller
      control={control}
      name={name}
      render={({
        field: { onChange, onBlur, value },
        fieldState: { isTouched, error },
        formState: { isSubmitted },
      }) => (
        <CustomAutocompleteDropdown
          {...autoCompleteProps}
          errorMessage={isTouched || isSubmitted ? error?.message : ''}
          value={value}
          onSelectItem={(item) => onChange(item)}
          onBlur={onBlur}
        />
      )}
    />
  );
};

type FormInputPropTypes = Omit<
  CustomAutocompleteDropdownPropTypesPropTypes,
  'ref' | 'onSelectItem' | 'onBlur' | 'controller' | 'value'
> & { name: string };

export default FormAutocompleteDropdown;
