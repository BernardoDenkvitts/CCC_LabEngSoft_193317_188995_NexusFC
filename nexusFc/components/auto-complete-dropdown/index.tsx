import React, { FC, useEffect, useRef } from 'react';
import {
  AutocompleteDropdown as OriginalAutocompleteDropdown,
  AutocompleteDropdownItem,
  IAutocompleteDropdownProps,
  IAutocompleteDropdownRef,
} from 'react-native-autocomplete-dropdown';
import { View } from 'react-native';
import { isEqual } from 'lodash';
import ErrorMessage from '../error-message';
import InputLabel from '../input/input-label';
import { Feather } from '@expo/vector-icons';

const CustomAutocompleteDropdown: FC<
  CustomAutocompleteDropdownPropTypesPropTypes
> = ({
  showChevron = true,
  required,
  direction = 'down',
  label,
  placeholder = label,
  value,
  errorMessage,
  backgroundColor,
  ...autoCompleteProps
}) => {
  let controller = useRef<IAutocompleteDropdownRef>(null);

  useEffect(() => {
    const currentItemInDataSet = autoCompleteProps.dataSet?.some((item) =>
      isEqual(value, item),
    );

    if (!currentItemInDataSet) controller.current?.clear();
  }, [value, autoCompleteProps.dataSet]);

  const prevValue = useRef(value);

  useEffect(() => {
    if (prevValue.current?.id === value?.id) return;
    prevValue.current = value;

    if (value) {
      controller.current?.setItem(value);
    } else {
      controller.current?.clear();
    }
  }, [value]);

  return (
    <>
      <InputLabel
        labelStyle={{ color: 'white' }}
        label={label}
        required={required}
      />
      <OriginalAutocompleteDropdown
        direction={direction}
        inputContainerStyle={{
          backgroundColor: 'white',
          padding: 3,
        }}
        suggestionsListTextStyle={{
          backgroundColor: 'white',
          color: 'black',
          fontFamily: 'Fredoka-Regular',
        }}
        suggestionsListContainerStyle={{
          backgroundColor: 'white',
          borderTopRightRadius: 0,
          borderTopLeftRadius: 0,
          shadowColor: '#000000',
          shadowOffset: { width: 0, height: 2 },
          shadowOpacity: 0.17,
          shadowRadius: 2.54,
          elevation: 3,
        }}
        ChevronIconComponent={
          <Feather name="chevron-down" size={24} color="black" />
        }
        ClearIconComponent={<Feather name="x-circle" size={20} color="black" />}
        ItemSeparatorComponent={() => (
          <View className="border border-gray-200" />
        )}
        showChevron={showChevron}
        {...autoCompleteProps}
        emptyResultText={
          autoCompleteProps.emptyResultText ?? 'Nenhum item encontrado.'
        }
        textInputProps={{
          placeholder: placeholder,
          autoCorrect: false,
          autoCapitalize: 'none',
          style: {
            backgroundColor: 'white',
            color: 'black',
          },
        }}
        controller={controller}
      />
      <ErrorMessage message={errorMessage} />
    </>
  );
};

export type CustomAutocompleteDropdownPropTypesPropTypes = Omit<
  IAutocompleteDropdownProps,
  'ref' | 'controller'
> & {
  placeholder?: string;
  label?: string;
  required?: boolean;
  value?: AutocompleteDropdownItem;
  errorMessage?: string;
  backgroundColor?: string;
};

export default CustomAutocompleteDropdown;
