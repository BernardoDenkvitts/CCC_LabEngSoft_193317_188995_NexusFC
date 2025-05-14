import React, { FC } from 'react';
import {
  StyleProp,
  Text,
  TextInput,
  TextInputProps,
  TextStyle,
} from 'react-native';

const InputLabel: FC<InputPropTypes> = ({
  label,
  labelStyle,
  inputStyle,
  ...inputProps
}) => {
  return (
    <>
      <Text style={labelStyle}>{label}:</Text>
      <TextInput style={inputStyle} {...inputProps} />
    </>
  );
};

export default InputLabel;

export type InputPropTypes = TextInputProps & {
  label: string;
  labelStyle: StyleProp<TextStyle>;
  inputStyle: StyleProp<TextStyle>;
};
