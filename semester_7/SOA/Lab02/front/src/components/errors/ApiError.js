import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';

const ApiError = ({open, error, onClose}) => {
  return (
    <Dialog open={open} onClose={onClose}>
        <DialogTitle>Some error happend</DialogTitle>
        <DialogContent>
          <DialogContentText>
            {error && error.message}
          </DialogContentText>
        </DialogContent>
        <Button onClick={onClose}>Close</Button>
      </Dialog>
    )
};

export default ApiError;