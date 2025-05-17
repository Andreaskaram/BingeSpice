package com.example.bingespice_app;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ResizeHelper {

    public static void addResizeListener(Stage stage) {
        new ResizeListener(stage);
    }

    private static class ResizeListener implements EventHandler<MouseEvent> {
        private final Stage stage;
        private Cursor cursorEvent = Cursor.DEFAULT;
        private final int border = 4;
        private double startX, startY;
        private double initialStageX, initialStageY;
        private double initialStageWidth, initialStageHeight;

        public ResizeListener(Stage stage) {
            this.stage = stage;
            Scene scene = stage.getScene();
            scene.addEventHandler(MouseEvent.MOUSE_MOVED, this);
            scene.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
            scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
        }

        @Override
        public void handle(MouseEvent event) {
            EventType<? extends MouseEvent> type = event.getEventType();

            if (type == MouseEvent.MOUSE_MOVED) {
                updateCursor(event.getSceneX(), event.getSceneY());
            }
            else if (type == MouseEvent.MOUSE_PRESSED) {
                startX = event.getScreenX();
                startY = event.getScreenY();
                initialStageX = stage.getX();
                initialStageY = stage.getY();
                initialStageWidth = stage.getWidth();
                initialStageHeight = stage.getHeight();
            }
            else if (type == MouseEvent.MOUSE_DRAGGED) {
                if (cursorEvent != Cursor.DEFAULT) {
                    handleResize(event);
                }
            }
        }

        private void updateCursor(double x, double y) {
            Scene scene = stage.getScene();

            if (y < border) {
                cursorEvent = Cursor.N_RESIZE;
            } else if (x < border) {
                cursorEvent = Cursor.W_RESIZE;
            } else if (x > scene.getWidth() - border) {
                cursorEvent = Cursor.E_RESIZE;
            } else if (y > scene.getHeight() - border) {
                cursorEvent = Cursor.S_RESIZE;
            } else {
                cursorEvent = Cursor.DEFAULT;
            }
            scene.setCursor(cursorEvent);
        }

        private void handleResize(MouseEvent event) {
            double minWidth = Math.max(stage.getMinWidth(), border * 2);
            double minHeight = Math.max(stage.getMinHeight(), border * 2);
            double deltaX = event.getScreenX() - startX;
            double deltaY = event.getScreenY() - startY;

            // Horizontal resizing
            if (cursorEvent == Cursor.W_RESIZE) { // Left edge
                double newWidth = initialStageWidth - deltaX;
                if (newWidth >= minWidth) {
                    stage.setWidth(newWidth);
                    stage.setX(initialStageX + deltaX);
                }
            }
            else if (cursorEvent == Cursor.E_RESIZE) { // Right edge
                double newWidth = initialStageWidth + deltaX;
                if (newWidth >= minWidth) {
                    stage.setWidth(newWidth);
                }
            }

            // Vertical resizing
            if (cursorEvent == Cursor.N_RESIZE) { // Top edge
                double newHeight = initialStageHeight - deltaY;
                if (newHeight >= minHeight) {
                    stage.setHeight(newHeight);
                    stage.setY(initialStageY + deltaY);
                }
            }
            else if (cursorEvent == Cursor.S_RESIZE) { // Bottom edge
                double newHeight = initialStageHeight + deltaY;
                if (newHeight >= minHeight) {
                    stage.setHeight(newHeight);
                }
            }
        }
    }
}