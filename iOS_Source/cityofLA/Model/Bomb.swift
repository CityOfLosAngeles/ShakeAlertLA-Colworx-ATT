//
//  Bomb.swift
//  cityofLA
//
//  Created by Sam Sidd on 16/11/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

import Foundation
import GoogleMaps;
import UIKit;

@objc class Bomb : NSObject {
    
    var title = "asdasdasd";
    @objc func bombOnCoordinate(m : GMSMarker , mapView : GMSMapView){
        
        //custom marker image
        let pulseRingImg = UIImageView(frame: CGRect(x: -30, y: -30, width: 78, height: 78))
        pulseRingImg.image = UIImage(named: "Pulse")
        pulseRingImg.isUserInteractionEnabled = false
        CATransaction.begin()
        CATransaction.setAnimationDuration(3.5)
        
        //transform scale animation
        var theAnimation: CABasicAnimation?
        theAnimation = CABasicAnimation(keyPath: "transform.scale.xy")
        theAnimation?.repeatCount = Float.infinity
        theAnimation?.autoreverses = false
        theAnimation?.fromValue = Float(0.0)
        theAnimation?.toValue = Float(2.0)
        theAnimation?.isRemovedOnCompletion = false
        
        pulseRingImg.layer.add(theAnimation!, forKey: "pulse")
        pulseRingImg.isUserInteractionEnabled = false
        CATransaction.setCompletionBlock({() -> Void in
            
            //alpha Animation for the image
            let animation = CAKeyframeAnimation(keyPath: "opacity")
            animation.duration = 3.5
            animation.repeatCount = Float.infinity
            animation.values = [Float(2.0), Float(0.0)]
            m.iconView?.layer.add(animation, forKey: "opacity")
        })
        
        CATransaction.commit()
        m.iconView = pulseRingImg
        m.layer.addSublayer(pulseRingImg.layer)
        m.map = mapView
        m.groundAnchor = CGPoint(x: 0.5, y: 0.5)
    }

}

