export const empty = {
  array: Object.seal<never[]>([]),
  object: Object.seal({}),
} as const;