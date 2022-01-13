function [SensedTransformed] = myAffineTran( tranVec, sensedImg ,referencedImgSize )
%UNTITLED3 Summary of this function goes here
%   Detailed explanation goes here
    
    affineTransformation = AffineTransformation (tranVec);
    maxX = size (referencedImgSize,1);
    maxY = size (referencedImgSize,2);
    
    SensedTransformed = NaN (maxX,maxY);
   
    for row=1:size(sensedImg,1)
        for col=1:size(sensedImg,2)
            point=[row,col];
            [newX,newY] = affineTransformation.transformPoint (point);
            % check that the new coordinate is not out of bounds 
            % todo next: check how exectly to implemnt this overlap thing
            if (newX < 0 || newY < 0)
                %fprintf ("out of limits point - below zero %d, %d new values:  %d, %d \n", point, newX, newY)
                continue;
            end
            if (newX > maxX || newY > maxY)
                %fprintf ("out of limits point - larger than max vals %d, %d new values:  %d, %d \n", point, newX, newY)
                continue;
            end
            SensedTransformed (newX,newY) = sensedImg(row,col);
        end
    end
end

   
