import { FC } from 'react';
import { Text, TextStyle } from 'react-native';
import { StyleProp } from 'react-native';

const ErrorMessage: FC<PropTypes> = ({ message, style }) => (
  <Text style={[{ fontSize: 14, margin: 1, color: 'red' }, style]}>
    {message}
  </Text>
);

type PropTypes = {
  message: string | undefined;
  style?: StyleProp<TextStyle>;
};

export default ErrorMessage;
