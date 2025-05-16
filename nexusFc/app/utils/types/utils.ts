import { Schema } from 'yup';

type Merge<A, B> = {
  [K in keyof A | keyof B]: K extends keyof A & keyof B
    ? A[K] | B[K]
    : K extends keyof B
      ? B[K]
      : K extends keyof A
        ? A[K]
        : never;
};

export type FormValuesFromYupSchema<S extends Schema> =
  S extends Schema<infer T, any, infer Defaults>
    ? Expand<Merge<Defaults, T>>
    : never;

export type PartialWithUndefined<T> = {
  [P in keyof T]-?: T[P] | undefined;
};

export type Expand<T> = T extends infer O ? { [K in keyof O]: O[K] } : never;
