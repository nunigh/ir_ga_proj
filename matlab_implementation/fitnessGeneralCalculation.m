function [ res ] = fitnessGeneralCalculation( tranVec, sensedImg , referencedImg , fitnessFunction )
%UNTITLED4 Summary of this function goes here
%   Detailed explanation goes here

    [SensedTransformed,SensedTransformedRef] =  AffineTran( tranVec, sensedImg ,size(referencedImg) );
   
    res = fitnessFunction (SensedTransformed, referencedImg);
end

