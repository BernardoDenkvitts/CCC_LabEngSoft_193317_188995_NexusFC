import React, { FC } from 'react';
import { StyleProp, Text, TextStyle, View } from 'react-native';

const InputLabel: FC<InputLabelTypes> = ({
  required,
  label,
  labelStyle,
  labelClassName,
}) => {
  return (
    <View className="mb-3 flex-row">
      <Text style={[labelStyle, { fontSize: 16 }]} className={labelClassName}>
        {label}
      </Text>
      {required && (
        <Text style={{ fontSize: 16 }} className="text-red-600">
          {' *'}
        </Text>
      )}
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
