function [ result ] = calcNCCbyImgPath ( tranVec0, sensedImgPath , referencedImgPath) 
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
    sensedImgRead = im2double(imread(sensedImgPath));
    dimSensed = size(size(sensedImgRead));
    if dimSensed(2) == 3
        sensedImgRead = rgb2gray(sensedImgRead);
    end
    referencedImgRead = im2double(imread(referencedImgPath));
    dimRefrenced = size(size(referencedImgRead));
    if dimRefrenced(2) == 3
        referencedImgRead = rgb2gray(referencedImgRead);
    end
    
    result = fitnessGeneralCalculation (tranVec0,sensedImgRead,referencedImgRead,@NCC_abs);

end

