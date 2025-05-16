import React, { FC } from 'react';
import { Text, TextProps, TouchableOpacity } from 'react-native';
import { TouchableOpacityProps } from 'react-native-gesture-handler';

const Button: FC<PropTypes> = ({
  children,
  disabled,
  title = '',
  titleProps,
  ...props
}) => {
  const titleComponent = title ? (
    <Text
      {...titleProps}
      style={[
        {
          textAlign: 'center',
          color: 'white',
          width: '100%',
          fontWeight: 600,
          fontSize: 15,
        },
        titleProps ? titleProps.style : {},
      ]}
    >
      {title}
    </Text>
  ) : null;

  return (
    <TouchableOpacity
      {...props}
      disabled={disabled}
      style={[
        {
          borderRadius: 15,
          padding: 10,
          backgroundColor: '#6F89FA', //rever isso fixo
          flexDirection: 'row',
          justifyContent: 'center',
          alignSelf: 'center',
        },
        props.containerStyle,
      ]}
    >
      {titleComponent}
      {children}
    </TouchableOpacity>
  );
};

type PropTypes = TouchableOpacityProps & {
  loading?: boolean;
  title?: string;
  titleProps?: TextProps;
};

export default Button;
