/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *//*

package com.venkatesan.das.cardmanager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.venkatesan.das.cardmanager.camera.GraphicOverlay;
import com.venkatesan.das.cardmanager.OCRGraphic;

import java.util.ArrayList;

*/
/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 *//*

public class OCRDetectorProcessor implements Detector.Processor<TextBlock> {
    private GraphicOverlay<OCRGraphic> mGraphicOverlay;
    private ArrayList<String> realCards;

    OCRDetectorProcessor(GraphicOverlay<OCRGraphic> ocrGraphicOverlay, ArrayList<String> allCards) {
        mGraphicOverlay = ocrGraphicOverlay;
        realCards = new ArrayList();
        for(String card: allCards){
            realCards.add(card.toLowerCase());
        }
    }

    @Override
    public void release() {
        mGraphicOverlay.clear();
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                // Check for card name in allCards list.
                if(realCards.contains(item.getValue().toLowerCase())){
                    Log.d("OCR_Processor", "Found the card: " + item.getValue());
                }
            }
            OCRGraphic graphic = new OCRGraphic(mGraphicOverlay, item);
            mGraphicOverlay.add(graphic);
        }
    }
}
*/
