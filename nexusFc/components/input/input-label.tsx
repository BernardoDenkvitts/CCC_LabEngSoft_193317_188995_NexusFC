import React, { FC } from 'react';
import { StyleProp, Text, TextStyle, View } from 'react-native';

const InputLabel: FC<InputLabelTypes> = ({
  required,
  label,
  labelStyle,
  labelClassName,
}) => {
  return (
    <View style={{ marginBottom: 3, flexDirection: 'row' }}>
      <Text style={[labelStyle, { fontSize: 16 }]} className={labelClassName}>
        {label}
      </Text>
      {required && <Text style={{ fontSize: 16, color: 'red' }}>{' *'}</Text>}
    </View>
  );
};

type InputLabelTypes = {
  required?: boolean;
  labelClassName?: string;
  label?: string;
  labelStyle?: StyleProp<TextStyle>;
};

export default InputLabel;
