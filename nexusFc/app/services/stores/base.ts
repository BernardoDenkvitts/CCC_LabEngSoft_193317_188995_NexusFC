import { isString, uniq } from 'lodash';
import { MMKV } from 'react-native-mmkv';

export default class StoreBase<T extends Record<string, any>> {
  #mmkv: MMKV;

  protected get mmkv() {
    return this.#mmkv;
  }

  protected set mmkv(newValue) {
    const prevKeys = this.getAllKeys();

    this.#mmkv = newValue;

    const newKeys = this.getAllKeys();

    uniq([...prevKeys, ...newKeys]).forEach((key) => this.#onValueChange(key));
  }

  constructor(mmkv: MMKV) {
    this.#mmkv = mmkv;
  }

  #listeners = new Set<ListenerCallback<T>>();

  #onValueChange(key: keyof T) {
    this.#listeners.forEach((listener) => listener(key));
  }

  protected parseToJSON<key extends keyof T>(value: string | undefined) {
    return isString(value) ? (JSON.parse(value) as T[key]) : value;
  }

  protected parseToString(value: T[keyof T]) {
    return JSON.stringify(value);
  }

  set<Key extends keyof T>(key: Key, value: T[Key]) {
    this.mmkv.set(key.toString(), this.parseToString(value));

    this.#onValueChange(key);
  }

  get<Key extends keyof T>(key: Key) {
    return this.parseToJSON<Key>(this.mmkv.getString(key.toString()));
  }

  delete<Key extends keyof T>(key: Key) {
    this.mmkv.delete(key.toString());

    this.#onValueChange(key);
  }

  getAllKeys() {
    return this.mmkv.getAllKeys() as (keyof T)[];
  }

  clearAll() {
    const keys = this.getAllKeys();
    this.mmkv.clearAll();

    keys.forEach((key) => this.#onValueChange(key));
  }

  addOnChange(callback: ListenerCallback<T>) {
    this.#listeners.add(callback);

    return {
      remove: () => {
        this.#listeners.delete(callback);
      },
    };
  }
}

type ListenerCallback<T extends Record<string, any>> = (key: keyof T) => void;
