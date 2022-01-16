function [ res ] = fitnessByHausdorffDist( tranVec, sensedImg , referencedImg )
%UNTITLED4 Summary of this function goes here
%   Detailed explanation goes here

    [SensedTransformed,SensedTransformedRef] =  AffineTran( tranVec, sensedImg ,size(referencedImg) );
    
    res = HausdorffDist (SensedTransformed, referencedImg);
end

