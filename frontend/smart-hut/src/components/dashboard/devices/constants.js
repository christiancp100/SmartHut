export const DEVICE_NAME_MAX_LENGTH = 15;

export function checkMaxLength(name) {
  return !(name.length > DEVICE_NAME_MAX_LENGTH);
}
