function [ result ] = myHD( tranVect, featuresReferenced, featuresSensed , limits)
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
    affineTransformation = AffineTransformation (tranVect);
    
    % tranVec: 7 float array
% featuresReferenced & featuresSensed : int matrix [2XN]
% limits: [maxX,maxY]

    maxX = limits (1);
    maxY = limits (2);
    
    
    randomIndexesList = randperm(size(featuresSensed,1));
    
    listlen = length (randomIndexesList);
    distArr = zeros(listlen,1);
    outboundcount=0;
    i=0;

    for col=randomIndexesList % iterate set rows in random order
        if (size (featuresReferenced,1)==0)
            % we get here  when size of sensed fetures list is larger then 
            % sensed fetaures lists
            break
        end
        point = featuresSensed (col,:);
        [newX,newY] = affineTransformation.transformPoint (point);
        % check that the new coordinate is not out of bounds 
        % todo next: check how exectly to implemnt this overlap thing
        if (newX < 0 || newY < 0)
            outboundcount= outboundcount+1;
            %fprintf ("out of limits point - below zero %d, %d new values:  %d, %d \n", point, newX, newY)
            continue;
        end
        if (newX > maxX || newY > maxY)
            outboundcount= outboundcount+1;
            %fprintf ("out of limits point - larger than max vals %d, %d new values:  %d, %d \n", point, newX, newY)
            continue;
        end

        %compute Euclidean distances:
        distances = sqrt(sum(bsxfun(@minus, featuresReferenced, [newX,newY]).^2,2));
        %find the smallest distance and use that as an index:
        closestIdxList = find(distances==min(distances));
        numOfResults = size (closestIdxList,1);
        if ( numOfResults > 1 )
            fprintf ("more than 1 neighbour. skipping %d", numOfResults)
            continue;
        end
        if ( numOfResults < 1 )
            fprintf ("less than 1 neighbour. num = %d \n", numOfResults)
        end
        closestIdx = closestIdxList (1);
        closestDist = distances(closestIdx,:);
        i=i+1;
        distArr(i) = closestDist;
        % remove the matched point from the list
        featuresReferenced (closestIdx,:) = [];         
    end
    result = median (distArr(1:i));
end

