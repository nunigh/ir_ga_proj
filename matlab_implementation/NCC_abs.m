function result = NCC_abs(X,Y)
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here
    ncc_val = my_NCC (X,Y);
    %if (ncc_val <0.25)
     %      result = [NaN,ncc_val];
    %else
            result = (-1) * ncc_val;
   % end
end

