import axios from 'axios';

const API_BASE = 'https://localhost:7500/api/v1/dragons';

export const getDragons = (filters) => {
  return axios.post(`${API_BASE}/array`, filters);
};

export const createDragon = (dragon) => {
  return axios.post(`${API_BASE}/`, dragon);
};

export const updateDragon = (dragonId, dragon) => {
  return axios.put(`${API_BASE}/${dragonId}`, dragon);
};

export const deleteDragon = (dragonId) => {
  return axios.delete(`${API_BASE}/${dragonId}`);
};

export const deleteAllDragonsByType = (type) => {
  return axios.delete(`${API_BASE}/all/type/${type}`);
};