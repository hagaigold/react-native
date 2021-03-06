/*
 *  Copyright (c) 2018-present, Facebook, Inc.
 *
 *  This source code is licensed under the MIT license found in the LICENSE
 *  file in the root directory of this source tree.
 *
 */
package com.facebook.yoga;

import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.soloader.SoLoader;

@DoNotStrip
public class YogaNodePropertiesJNI implements Cloneable, YogaNodeProperties {

  static {
    SoLoader.loadLibrary("yoga");
  }

  private long mNativePointer;

  /* Those flags needs be in sync with YGJNI.cpp */
  protected static final int MARGIN = 1;
  protected static final int PADDING = 2;
  protected static final int BORDER = 4;

  @DoNotStrip protected int mEdgeSetFlag = 0;

  protected boolean mHasSetPosition = false;

  @DoNotStrip private float mWidth = YogaConstants.UNDEFINED;
  @DoNotStrip private float mHeight = YogaConstants.UNDEFINED;
  @DoNotStrip private float mTop = YogaConstants.UNDEFINED;
  @DoNotStrip private float mLeft = YogaConstants.UNDEFINED;
  @DoNotStrip private float mMarginLeft = 0;
  @DoNotStrip private float mMarginTop = 0;
  @DoNotStrip private float mMarginRight = 0;
  @DoNotStrip private float mMarginBottom = 0;
  @DoNotStrip private float mPaddingLeft = 0;
  @DoNotStrip private float mPaddingTop = 0;
  @DoNotStrip private float mPaddingRight = 0;
  @DoNotStrip private float mPaddingBottom = 0;
  @DoNotStrip private float mBorderLeft = 0;
  @DoNotStrip private float mBorderTop = 0;
  @DoNotStrip private float mBorderRight = 0;
  @DoNotStrip private float mBorderBottom = 0;
  @DoNotStrip private int mLayoutDirection = 0;
  @DoNotStrip private boolean mHasNewLayout = true;
  @DoNotStrip private boolean mDoesLegacyStretchFlagAffectsLayout = false;

  private native long jni_YGNodeNew(YogaNode node);

  public YogaNodePropertiesJNI(YogaNode node) {
    mNativePointer = jni_YGNodeNew(node);
    if (mNativePointer == 0) {
      throw new IllegalStateException("Failed to allocate native memory");
    }
  }

  private native long jni_YGNodeNewWithConfig(YogaNode node, long configPointer);

  public YogaNodePropertiesJNI(YogaNode node, YogaConfig config) {
    mNativePointer = jni_YGNodeNewWithConfig(node, config.mNativePointer);
    if (mNativePointer == 0) {
      throw new IllegalStateException("Failed to allocate native memory");
    }
  }

  private static native void jni_YGNodeFree(long nativePointer);

  @Override
  public void freeNatives() {
    long nativePointer = mNativePointer;
    mNativePointer = 0;
    jni_YGNodeFree(nativePointer);
  }

  @Override
  protected void finalize() throws Throwable {
    try {
      freeNatives();
    } finally {
      super.finalize();
    }
  }

  private static native long jni_YGNodeClone(
      long nativePointer, YogaNode newNode, YogaNodePropertiesJNI newProps);

  @Override
  public YogaNodeProperties clone(YogaNode node) {
    try {
      YogaNodePropertiesJNI clonedProperties = (YogaNodePropertiesJNI) clone();
      clonedProperties.mNativePointer = jni_YGNodeClone(getNativePointer(), node, clonedProperties);
      return clonedProperties;
    } catch (CloneNotSupportedException ex) {
      // This class implements Cloneable, this should not happen
      throw new RuntimeException(ex);
    }
  }

  @Override
  public long getNativePointer() {
    return mNativePointer;
  }

  private static native void jni_YGTransferLayoutOutputsRecursive(long nativePointer);

  @Override
  public void onAfterCalculateLayout(boolean hasNewLayoutIgnoredSetByNative) {
    jni_YGTransferLayoutOutputsRecursive(mNativePointer);
  }

  private native void jni_YGNodeReset(long nativePointer);

  @Override
  public void reset() {
    mHasSetPosition = false;
    mEdgeSetFlag = 0;
    mHasNewLayout = true;
    mDoesLegacyStretchFlagAffectsLayout = false;

    mWidth = YogaConstants.UNDEFINED;
    mHeight = YogaConstants.UNDEFINED;
    mTop = YogaConstants.UNDEFINED;
    mLeft = YogaConstants.UNDEFINED;
    mMarginLeft = 0;
    mMarginTop = 0;
    mMarginRight = 0;
    mMarginBottom = 0;
    mPaddingLeft = 0;
    mPaddingTop = 0;
    mPaddingRight = 0;
    mPaddingBottom = 0;
    mBorderLeft = 0;
    mBorderTop = 0;
    mBorderRight = 0;
    mBorderBottom = 0;
    mLayoutDirection = 0;
    jni_YGNodeReset(getNativePointer());
  }

  @Override
  public boolean hasNewLayout() {
    return mHasNewLayout;
  }

  private native boolean jni_YGNodeIsDirty(long nativePointer);

  @Override
  public boolean isDirty() {
    return jni_YGNodeIsDirty(mNativePointer);
  }

  @Override
  public void markLayoutSeen() {
    mHasNewLayout = false;
  }

  private native int jni_YGNodeStyleGetDirection(long nativePointer);

  @Override
  public YogaDirection getStyleDirection() {
    return YogaDirection.fromInt(jni_YGNodeStyleGetDirection(mNativePointer));
  }

  private native void jni_YGNodeStyleSetDirection(long nativePointer, int direction);

  @Override
  public void setDirection(YogaDirection direction) {
    jni_YGNodeStyleSetDirection(mNativePointer, direction.intValue());
  }

  private native int jni_YGNodeStyleGetFlexDirection(long nativePointer);

  @Override
  public YogaFlexDirection getFlexDirection() {
    return YogaFlexDirection.fromInt(jni_YGNodeStyleGetFlexDirection(mNativePointer));
  }

  private native void jni_YGNodeStyleSetFlexDirection(long nativePointer, int flexDirection);

  @Override
  public void setFlexDirection(YogaFlexDirection flexDirection) {
    jni_YGNodeStyleSetFlexDirection(mNativePointer, flexDirection.intValue());
  }

  private native int jni_YGNodeStyleGetJustifyContent(long nativePointer);

  @Override
  public YogaJustify getJustifyContent() {
    return YogaJustify.fromInt(jni_YGNodeStyleGetJustifyContent(mNativePointer));
  }

  private native void jni_YGNodeStyleSetJustifyContent(long nativePointer, int justifyContent);

  @Override
  public void setJustifyContent(YogaJustify justifyContent) {
    jni_YGNodeStyleSetJustifyContent(mNativePointer, justifyContent.intValue());
  }

  private native int jni_YGNodeStyleGetAlignItems(long nativePointer);

  @Override
  public YogaAlign getAlignItems() {
    return YogaAlign.fromInt(jni_YGNodeStyleGetAlignItems(mNativePointer));
  }

  private native void jni_YGNodeStyleSetAlignItems(long nativePointer, int alignItems);

  @Override
  public void setAlignItems(YogaAlign alignItems) {
    jni_YGNodeStyleSetAlignItems(mNativePointer, alignItems.intValue());
  }

  private native int jni_YGNodeStyleGetAlignSelf(long nativePointer);

  @Override
  public YogaAlign getAlignSelf() {
    return YogaAlign.fromInt(jni_YGNodeStyleGetAlignSelf(mNativePointer));
  }

  private native void jni_YGNodeStyleSetAlignSelf(long nativePointer, int alignSelf);

  @Override
  public void setAlignSelf(YogaAlign alignSelf) {
    jni_YGNodeStyleSetAlignSelf(mNativePointer, alignSelf.intValue());
  }

  private native int jni_YGNodeStyleGetAlignContent(long nativePointer);

  @Override
  public YogaAlign getAlignContent() {
    return YogaAlign.fromInt(jni_YGNodeStyleGetAlignContent(mNativePointer));
  }

  private native void jni_YGNodeStyleSetAlignContent(long nativePointer, int alignContent);

  @Override
  public void setAlignContent(YogaAlign alignContent) {
    jni_YGNodeStyleSetAlignContent(mNativePointer, alignContent.intValue());
  }

  private native int jni_YGNodeStyleGetPositionType(long nativePointer);

  @Override
  public YogaPositionType getPositionType() {
    return YogaPositionType.fromInt(jni_YGNodeStyleGetPositionType(mNativePointer));
  }

  private native void jni_YGNodeStyleSetPositionType(long nativePointer, int positionType);

  @Override
  public void setPositionType(YogaPositionType positionType) {
    jni_YGNodeStyleSetPositionType(mNativePointer, positionType.intValue());
  }

  private native void jni_YGNodeStyleSetFlexWrap(long nativePointer, int wrapType);

  @Override
  public void setWrap(YogaWrap flexWrap) {
    jni_YGNodeStyleSetFlexWrap(mNativePointer, flexWrap.intValue());
  }

  private native int jni_YGNodeStyleGetOverflow(long nativePointer);

  @Override
  public YogaOverflow getOverflow() {
    return YogaOverflow.fromInt(jni_YGNodeStyleGetOverflow(mNativePointer));
  }

  private native void jni_YGNodeStyleSetOverflow(long nativePointer, int overflow);

  @Override
  public void setOverflow(YogaOverflow overflow) {
    jni_YGNodeStyleSetOverflow(mNativePointer, overflow.intValue());
  }

  private native int jni_YGNodeStyleGetDisplay(long nativePointer);

  @Override
  public YogaDisplay getDisplay() {
    return YogaDisplay.fromInt(jni_YGNodeStyleGetDisplay(mNativePointer));
  }

  private native void jni_YGNodeStyleSetDisplay(long nativePointer, int display);

  @Override
  public void setDisplay(YogaDisplay display) {
    jni_YGNodeStyleSetDisplay(mNativePointer, display.intValue());
  }

  private native void jni_YGNodeStyleSetFlex(long nativePointer, float flex);

  @Override
  public void setFlex(float flex) {
    jni_YGNodeStyleSetFlex(mNativePointer, flex);
  }

  private native float jni_YGNodeStyleGetFlexGrow(long nativePointer);

  @Override
  public float getFlexGrow() {
    return jni_YGNodeStyleGetFlexGrow(mNativePointer);
  }

  private native void jni_YGNodeStyleSetFlexGrow(long nativePointer, float flexGrow);

  @Override
  public void setFlexGrow(float flexGrow) {
    jni_YGNodeStyleSetFlexGrow(mNativePointer, flexGrow);
  }

  private native float jni_YGNodeStyleGetFlexShrink(long nativePointer);

  @Override
  public float getFlexShrink() {
    return jni_YGNodeStyleGetFlexShrink(mNativePointer);
  }

  private native void jni_YGNodeStyleSetFlexShrink(long nativePointer, float flexShrink);

  @Override
  public void setFlexShrink(float flexShrink) {
    jni_YGNodeStyleSetFlexShrink(mNativePointer, flexShrink);
  }

  private native Object jni_YGNodeStyleGetFlexBasis(long nativePointer);

  @Override
  public YogaValue getFlexBasis() {
    return (YogaValue) jni_YGNodeStyleGetFlexBasis(mNativePointer);
  }

  private native void jni_YGNodeStyleSetFlexBasis(long nativePointer, float flexBasis);

  @Override
  public void setFlexBasis(float flexBasis) {
    jni_YGNodeStyleSetFlexBasis(mNativePointer, flexBasis);
  }

  private native void jni_YGNodeStyleSetFlexBasisPercent(long nativePointer, float percent);

  @Override
  public void setFlexBasisPercent(float percent) {
    jni_YGNodeStyleSetFlexBasisPercent(mNativePointer, percent);
  }

  private native void jni_YGNodeStyleSetFlexBasisAuto(long nativePointer);

  @Override
  public void setFlexBasisAuto() {
    jni_YGNodeStyleSetFlexBasisAuto(mNativePointer);
  }

  private native Object jni_YGNodeStyleGetMargin(long nativePointer, int edge);

  @Override
  public YogaValue getMargin(YogaEdge edge) {
    if (!((mEdgeSetFlag & MARGIN) == MARGIN)) {
      return YogaValue.UNDEFINED;
    }
    return (YogaValue) jni_YGNodeStyleGetMargin(mNativePointer, edge.intValue());
  }

  private native void jni_YGNodeStyleSetMargin(long nativePointer, int edge, float margin);

  @Override
  public void setMargin(YogaEdge edge, float margin) {
    mEdgeSetFlag |= MARGIN;
    jni_YGNodeStyleSetMargin(mNativePointer, edge.intValue(), margin);
  }

  private native void jni_YGNodeStyleSetMarginPercent(long nativePointer, int edge, float percent);

  @Override
  public void setMarginPercent(YogaEdge edge, float percent) {
    mEdgeSetFlag |= MARGIN;
    jni_YGNodeStyleSetMarginPercent(mNativePointer, edge.intValue(), percent);
  }

  private native void jni_YGNodeStyleSetMarginAuto(long nativePointer, int edge);

  @Override
  public void setMarginAuto(YogaEdge edge) {
    mEdgeSetFlag |= MARGIN;
    jni_YGNodeStyleSetMarginAuto(mNativePointer, edge.intValue());
  }

  private native Object jni_YGNodeStyleGetPadding(long nativePointer, int edge);

  @Override
  public YogaValue getPadding(YogaEdge edge) {
    if (!((mEdgeSetFlag & PADDING) == PADDING)) {
      return YogaValue.UNDEFINED;
    }
    return (YogaValue) jni_YGNodeStyleGetPadding(mNativePointer, edge.intValue());
  }

  private native void jni_YGNodeStyleSetPadding(long nativePointer, int edge, float padding);

  @Override
  public void setPadding(YogaEdge edge, float padding) {
    mEdgeSetFlag |= PADDING;
    jni_YGNodeStyleSetPadding(mNativePointer, edge.intValue(), padding);
  }

  private native void jni_YGNodeStyleSetPaddingPercent(long nativePointer, int edge, float percent);

  @Override
  public void setPaddingPercent(YogaEdge edge, float percent) {
    mEdgeSetFlag |= PADDING;
    jni_YGNodeStyleSetPaddingPercent(mNativePointer, edge.intValue(), percent);
  }

  private native float jni_YGNodeStyleGetBorder(long nativePointer, int edge);

  @Override
  public float getBorder(YogaEdge edge) {
    if (!((mEdgeSetFlag & BORDER) == BORDER)) {
      return YogaConstants.UNDEFINED;
    }
    return jni_YGNodeStyleGetBorder(mNativePointer, edge.intValue());
  }

  private native void jni_YGNodeStyleSetBorder(long nativePointer, int edge, float border);

  @Override
  public void setBorder(YogaEdge edge, float border) {
    mEdgeSetFlag |= BORDER;
    jni_YGNodeStyleSetBorder(mNativePointer, edge.intValue(), border);
  }

  private native Object jni_YGNodeStyleGetPosition(long nativePointer, int edge);

  @Override
  public YogaValue getPosition(YogaEdge edge) {
    if (!mHasSetPosition) {
      return YogaValue.UNDEFINED;
    }
    return (YogaValue) jni_YGNodeStyleGetPosition(mNativePointer, edge.intValue());
  }

  private native void jni_YGNodeStyleSetPosition(long nativePointer, int edge, float position);

  @Override
  public void setPosition(YogaEdge edge, float position) {
    mHasSetPosition = true;
    jni_YGNodeStyleSetPosition(mNativePointer, edge.intValue(), position);
  }

  private native void jni_YGNodeStyleSetPositionPercent(
      long nativePointer, int edge, float percent);

  @Override
  public void setPositionPercent(YogaEdge edge, float percent) {
    mHasSetPosition = true;
    jni_YGNodeStyleSetPositionPercent(mNativePointer, edge.intValue(), percent);
  }

  private native Object jni_YGNodeStyleGetWidth(long nativePointer);

  @Override
  public YogaValue getWidth() {
    return (YogaValue) jni_YGNodeStyleGetWidth(mNativePointer);
  }

  private native void jni_YGNodeStyleSetWidth(long nativePointer, float width);

  @Override
  public void setWidth(float width) {
    jni_YGNodeStyleSetWidth(mNativePointer, width);
  }

  private native void jni_YGNodeStyleSetWidthPercent(long nativePointer, float percent);

  @Override
  public void setWidthPercent(float percent) {
    jni_YGNodeStyleSetWidthPercent(mNativePointer, percent);
  }

  private native void jni_YGNodeStyleSetWidthAuto(long nativePointer);

  @Override
  public void setWidthAuto() {
    jni_YGNodeStyleSetWidthAuto(mNativePointer);
  }

  private native Object jni_YGNodeStyleGetHeight(long nativePointer);

  @Override
  public YogaValue getHeight() {
    return (YogaValue) jni_YGNodeStyleGetHeight(mNativePointer);
  }

  private native void jni_YGNodeStyleSetHeight(long nativePointer, float height);

  @Override
  public void setHeight(float height) {
    jni_YGNodeStyleSetHeight(mNativePointer, height);
  }

  private native void jni_YGNodeStyleSetHeightPercent(long nativePointer, float percent);

  @Override
  public void setHeightPercent(float percent) {
    jni_YGNodeStyleSetHeightPercent(mNativePointer, percent);
  }

  private native void jni_YGNodeStyleSetHeightAuto(long nativePointer);

  @Override
  public void setHeightAuto() {
    jni_YGNodeStyleSetHeightAuto(mNativePointer);
  }

  private native Object jni_YGNodeStyleGetMinWidth(long nativePointer);

  @Override
  public YogaValue getMinWidth() {
    return (YogaValue) jni_YGNodeStyleGetMinWidth(mNativePointer);
  }

  private native void jni_YGNodeStyleSetMinWidth(long nativePointer, float minWidth);

  @Override
  public void setMinWidth(float minWidth) {
    jni_YGNodeStyleSetMinWidth(mNativePointer, minWidth);
  }

  private native void jni_YGNodeStyleSetMinWidthPercent(long nativePointer, float percent);

  @Override
  public void setMinWidthPercent(float percent) {
    jni_YGNodeStyleSetMinWidthPercent(mNativePointer, percent);
  }

  private native Object jni_YGNodeStyleGetMinHeight(long nativePointer);

  @Override
  public YogaValue getMinHeight() {
    return (YogaValue) jni_YGNodeStyleGetMinHeight(mNativePointer);
  }

  private native void jni_YGNodeStyleSetMinHeight(long nativePointer, float minHeight);

  @Override
  public void setMinHeight(float minHeight) {
    jni_YGNodeStyleSetMinHeight(mNativePointer, minHeight);
  }

  private native void jni_YGNodeStyleSetMinHeightPercent(long nativePointer, float percent);

  @Override
  public void setMinHeightPercent(float percent) {
    jni_YGNodeStyleSetMinHeightPercent(mNativePointer, percent);
  }

  private native Object jni_YGNodeStyleGetMaxWidth(long nativePointer);

  @Override
  public YogaValue getMaxWidth() {
    return (YogaValue) jni_YGNodeStyleGetMaxWidth(mNativePointer);
  }

  private native void jni_YGNodeStyleSetMaxWidth(long nativePointer, float maxWidth);

  @Override
  public void setMaxWidth(float maxWidth) {
    jni_YGNodeStyleSetMaxWidth(mNativePointer, maxWidth);
  }

  private native void jni_YGNodeStyleSetMaxWidthPercent(long nativePointer, float percent);

  @Override
  public void setMaxWidthPercent(float percent) {
    jni_YGNodeStyleSetMaxWidthPercent(mNativePointer, percent);
  }

  private native Object jni_YGNodeStyleGetMaxHeight(long nativePointer);

  @Override
  public YogaValue getMaxHeight() {
    return (YogaValue) jni_YGNodeStyleGetMaxHeight(mNativePointer);
  }

  private native void jni_YGNodeStyleSetMaxHeight(long nativePointer, float maxheight);

  @Override
  public void setMaxHeight(float maxheight) {
    jni_YGNodeStyleSetMaxHeight(mNativePointer, maxheight);
  }

  private native void jni_YGNodeStyleSetMaxHeightPercent(long nativePointer, float percent);

  @Override
  public void setMaxHeightPercent(float percent) {
    jni_YGNodeStyleSetMaxHeightPercent(mNativePointer, percent);
  }

  private native float jni_YGNodeStyleGetAspectRatio(long nativePointer);

  @Override
  public float getAspectRatio() {
    return jni_YGNodeStyleGetAspectRatio(mNativePointer);
  }

  private native void jni_YGNodeStyleSetAspectRatio(long nativePointer, float aspectRatio);

  @Override
  public void setAspectRatio(float aspectRatio) {
    jni_YGNodeStyleSetAspectRatio(mNativePointer, aspectRatio);
  }

  @Override
  public float getLayoutX() {
    return mLeft;
  }

  @Override
  public float getLayoutY() {
    return mTop;
  }

  @Override
  public float getLayoutWidth() {
    return mWidth;
  }

  @Override
  public float getLayoutHeight() {
    return mHeight;
  }

  @Override
  public boolean getDoesLegacyStretchFlagAffectsLayout() {
    return mDoesLegacyStretchFlagAffectsLayout;
  }

  @Override
  public float getLayoutMargin(YogaEdge edge) {
    switch (edge) {
      case LEFT:
        return mMarginLeft;
      case TOP:
        return mMarginTop;
      case RIGHT:
        return mMarginRight;
      case BOTTOM:
        return mMarginBottom;
      case START:
        return getLayoutDirection() == YogaDirection.RTL ? mMarginRight : mMarginLeft;
      case END:
        return getLayoutDirection() == YogaDirection.RTL ? mMarginLeft : mMarginRight;
      default:
        throw new IllegalArgumentException("Cannot get layout margins of multi-edge shorthands");
    }
  }

  @Override
  public float getLayoutPadding(YogaEdge edge) {
    switch (edge) {
      case LEFT:
        return mPaddingLeft;
      case TOP:
        return mPaddingTop;
      case RIGHT:
        return mPaddingRight;
      case BOTTOM:
        return mPaddingBottom;
      case START:
        return getLayoutDirection() == YogaDirection.RTL ? mPaddingRight : mPaddingLeft;
      case END:
        return getLayoutDirection() == YogaDirection.RTL ? mPaddingLeft : mPaddingRight;
      default:
        throw new IllegalArgumentException("Cannot get layout paddings of multi-edge shorthands");
    }
  }

  @Override
  public float getLayoutBorder(YogaEdge edge) {
    switch (edge) {
      case LEFT:
        return mBorderLeft;
      case TOP:
        return mBorderTop;
      case RIGHT:
        return mBorderRight;
      case BOTTOM:
        return mBorderBottom;
      case START:
        return getLayoutDirection() == YogaDirection.RTL ? mBorderRight : mBorderLeft;
      case END:
        return getLayoutDirection() == YogaDirection.RTL ? mBorderLeft : mBorderRight;
      default:
        throw new IllegalArgumentException("Cannot get layout border of multi-edge shorthands");
    }
  }

  @Override
  public YogaDirection getLayoutDirection() {
    return YogaDirection.fromInt(mLayoutDirection);
  }
}
