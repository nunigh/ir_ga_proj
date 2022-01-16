function [ result ] = my_NCC( img1,img2 )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
img1 = im2double(img1);
img2 = im2double(img2);
% to do: take into account overlap and dimentions
img1vec = img1(~isinf(img1+img2));
img2vec = img2(~isinf(img1+img2));

avg1 = mean2(img1vec);
avg2 = mean2(img2vec);

diffFromAvg1 = img1vec-avg1;
diffFromAvg2 = img2vec-avg2;
upperSum = sum(sum(diffFromAvg1.* diffFromAvg2));

lowerSum1 = sum(sum(diffFromAvg1.*diffFromAvg1));
lowerSum2 = sum(sum(diffFromAvg2.*diffFromAvg2));

result = upperSum/((lowerSum1*lowerSum2)^0.5);

end

