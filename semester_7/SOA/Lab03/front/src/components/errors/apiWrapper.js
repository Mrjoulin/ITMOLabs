import { useState, useEffect } from 'react';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';

const ApiWrapper = () => {
  const [openError, setOpen] = useState(false);
  const [error, setError] = useState(null);

  const handleClose = () => {
    setOpen(false);
  };

  const handleError = (error) => {
    setError(error);
    setOpen(true);
  };

  const wrapApiCall = (apiCall) => {
    return async (...args) => {
      try {
        const response = await apiCall(...args);
        if (response.status >= 200 && response.status < 300) {
          return response;
        } else {
          handleError(response.data);
          throw new Error(response.data.message);
        }
      } catch (error) {
        if (error.response) {
          handleError(error.response.data);
        } else {
          handleError({ message: error.message });
        }
        throw error;
      }
    };
  };

  return {
    wrapApiCall,
    ErrorDialog: () => (
      <Dialog open={openError} onClose={handleClose}>
        <DialogTitle>Some error happend</DialogTitle>
        <DialogContent>
          <DialogContentText>
            {error && error.message}
          </DialogContentText>
        </DialogContent>
        <Button onClick={handleClose}>Close</Button>
      </Dialog>
    ),
  };
};

export default ApiWrapper;