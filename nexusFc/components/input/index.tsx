import React, { FC, useState } from 'react';
import {
  StyleProp,
  TextInput,
  TextInputProps,
  TextStyle,
  ViewStyle,
  TouchableOpacity,
  View,
  ActivityIndicator,
} from 'react-native';
import Ionicons from '@expo/vector-icons/Ionicons';
// import ErrorMessage from '../error-message';
import InputLabel from './input-label';
import ErrorMessage from '../error-message';

const Input: FC<InputPropTypes> = ({
  className,
  style,
  required,
  containerClassName,
  containerStyle,
  label,
  labelClassName,
  labelStyle,
  errorMessage,
  errorMessageStyle,
  loading,
  backgroundColor,
  ...inputProps
}) => {
  const [secureTextEntryEnabled, setSecureTextEntryEnabled] = useState(
    inputProps.secureTextEntry ?? false,
  );

  return (
    <View style={containerStyle} className={containerClassName}>
      <InputLabel
        label={label}
        labelClassName={labelClassName}
        required={required}
        labelStyle={labelStyle}
      />
      <View>
        <TextInput
          placeholderTextColor="#86939e"
          underlineColorAndroid="transparent"
          {...inputProps}
          style={[
            {
              fontSize: 18,
              padding: 10,
              borderRadius: 15,
              backgroundColor: 'white',
              minHeight: 15,
            },
            style,
          ]}
          secureTextEntry={
            inputProps?.secureTextEntry ? secureTextEntryEnabled : false
          }
        />
        <View
          style={{
            position: 'absolute',
            flexDirection: 'row',
            alignItems: 'center',
            height: '100%',
            gap: 4,
            right: 0,
            paddingRight: 5,
          }}
        >
          {loading && <ActivityIndicator />}

          {inputProps?.secureTextEntry && (
            <TouchableOpacity
              onPress={() => setSecureTextEntryEnabled((prev) => !prev)}
            >
              <View className="">
                {secureTextEntryEnabled ? (
                  <Ionicons name="eye-off" size={24} color="black" />
                ) : (
                  <Ionicons name="eye" color="#black" size={24} />
                )}
              </View>
            </TouchableOpacity>
          )}
        </View>
      </View>
      <ErrorMessage message={errorMessage} style={errorMessageStyle} />
    </View>
  );
};

export type InputPropTypes = TextInputProps & {
  required?: boolean;
  className?: string;
  style?: StyleProp<TextStyle>;
  containerClassName?: string;
  containerStyle?: StyleProp<ViewStyle>;
  label?: string;
  labelClassName?: string;
  labelStyle?: StyleProp<TextStyle>;
  errorMessage?: string;
  errorMessageStyle?: StyleProp<TextStyle>;
  loading?: boolean;
  backgroundColor?: string;
};

export default Input;
