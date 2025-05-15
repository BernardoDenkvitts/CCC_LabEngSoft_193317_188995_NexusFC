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
  errorMessageClassName,
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
          style={style}
          className={`
                text-[18px] min-h-10 p-4 rounded-lg ${backgroundColor ?? 'bg-orange-100'}
                ${className}
              `}
          secureTextEntry={
            inputProps?.secureTextEntry ? secureTextEntryEnabled : false
          }
        />
        <View className="absolute right-0 flex-row items-center h-full pr-4 gap-3">
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
      {/* <ErrorMessage
        message={errorMessage}
        className={errorMessageClassName}
        style={errorMessageStyle}
      /> */}
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
  errorMessageClassName?: string;
  errorMessageStyle?: StyleProp<TextStyle>;
  loading?: boolean;
  backgroundColor?: string;
};

export default Input;
