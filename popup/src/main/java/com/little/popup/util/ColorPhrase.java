package com.little.popup.util;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import java.util.Stack;

/**
 * 描述
 * <p>Version: v1.0</p>
 * <p>Created by: litangyu</p>
 * <p>Created on: 2016/12/1 下午3:45</p>
 * <p>Email: lty81372860@sina.com</p>
 * <p>Copyright © 2016年 litangyu. All rights reserved.</p>
 * <p>Revision：</p>
 */
public class ColorPhrase {private final CharSequence pattern;
    private CharSequence formatted;
    private ColorPhrase.Token head;
    private char curChar;
    private String separator;
    private int curCharIndex;
    private int outerColor;
    private int innerColor;
    private static final int EOF = 0;

    @TargetApi(11)
    public static ColorPhrase from(Fragment var0, int var1) {
        return from(var0.getResources(), var1);
    }

    public static ColorPhrase from(View var0, int var1) {
        return from(var0.getResources(), var1);
    }

    public static ColorPhrase from(Context var0, int var1) {
        return from(var0.getResources(), var1);
    }

    public static ColorPhrase from(Resources var0, int var1) {
        return from(var0.getText(var1));
    }

    public static ColorPhrase from(CharSequence var0) {
        return new ColorPhrase(var0);
    }

    private ColorPhrase(CharSequence var1) {
        this.curChar = var1.length() > 0?var1.charAt(0):0;
        this.pattern = var1;
        this.formatted = null;
        this.separator = "{}";
        this.outerColor = -10066330;
        this.innerColor = -1686198;
    }

    public ColorPhrase withSeparator(String var1) {
        if(TextUtils.isEmpty(var1)) {
            throw new IllegalArgumentException("separator must not be empty!");
        } else if(var1.length() > 2) {
            throw new IllegalArgumentException("separator‘s length must not be more than 3 charactors!");
        } else {
            this.separator = var1;
            return this;
        }
    }

    public ColorPhrase outerColor(int var1) {
        this.outerColor = var1;
        return this;
    }

    public ColorPhrase innerColor(int var1) {
        this.innerColor = var1;
        return this;
    }

    private void createDoubleLinkWithToken() {
        ColorPhrase.Token var2;
        for(ColorPhrase.Token var1 = null; (var2 = this.token(var1)) != null; var1 = var2) {
            if(this.head == null) {
                this.head = var2;
            }
        }

    }

    private ColorPhrase.Token token(ColorPhrase.Token var1) {
        if(this.curChar == 0) {
            return null;
        } else if(this.curChar == this.getLeftSeparator()) {
            char var2 = this.lookahead();
            return (ColorPhrase.Token)(var2 == this.getLeftSeparator()?this.leftSeparator(var1):this.inner(var1));
        } else {
            return this.outer(var1);
        }
    }

    private char getLeftSeparator() {
        return this.separator.charAt(0);
    }

    private char getRightSeparator() {
        return this.separator.length() == 2?this.separator.charAt(1):this.separator.charAt(0);
    }

    public CharSequence format() {
        if(this.formatted == null) {
            if(!this.checkPattern()) {
                throw new IllegalStateException("the separators don\'t match in the pattern!");
            }

            this.createDoubleLinkWithToken();
            SpannableStringBuilder var1 = new SpannableStringBuilder(this.pattern);

            for(ColorPhrase.Token var2 = this.head; var2 != null; var2 = var2.next) {
                var2.expand(var1);
            }

            this.formatted = var1;
        }

        return this.formatted;
    }

    private boolean checkPattern() {
        if(this.pattern == null) {
            return false;
        } else {
            char var1 = this.getLeftSeparator();
            char var2 = this.getRightSeparator();
            Stack var3 = new Stack();

            for(int var4 = 0; var4 < this.pattern.length(); ++var4) {
                char var5 = this.pattern.charAt(var4);
                if(var5 == var1) {
                    var3.push(Character.valueOf(var5));
                } else if(var5 == var2 && (var3.isEmpty() || ((Character)var3.pop()).charValue() != var1)) {
                    return false;
                }
            }

            return var3.isEmpty();
        }
    }

    private ColorPhrase.InnerToken inner(ColorPhrase.Token var1) {
        StringBuilder var2 = new StringBuilder();
        this.consume();
        char var3 = this.getRightSeparator();

        while(this.curChar != var3 && this.curChar != 0) {
            var2.append(this.curChar);
            this.consume();
        }

        if(this.curChar == 0) {
            throw new IllegalArgumentException("Missing closing separator");
        } else {
            this.consume();
            if(var2.length() == 0) {
                throw new IllegalStateException("Disallow empty content between separators,for example {}");
            } else {
                String var4 = var2.toString();
                return new ColorPhrase.InnerToken(var1, var4, this.innerColor);
            }
        }
    }

    private ColorPhrase.OuterToken outer(ColorPhrase.Token var1) {
        int var2 = this.curCharIndex;

        while(this.curChar != this.getLeftSeparator() && this.curChar != 0) {
            this.consume();
        }

        return new ColorPhrase.OuterToken(var1, this.curCharIndex - var2, this.outerColor);
    }

    private ColorPhrase.LeftSeparatorToken leftSeparator(ColorPhrase.Token var1) {
        this.consume();
        this.consume();
        return new ColorPhrase.LeftSeparatorToken(var1, this.getLeftSeparator());
    }

    private char lookahead() {
        return this.curCharIndex < this.pattern.length() - 1?this.pattern.charAt(this.curCharIndex + 1):'\u0000';
    }

    private void consume() {
        ++this.curCharIndex;
        this.curChar = this.curCharIndex == this.pattern.length()?0:this.pattern.charAt(this.curCharIndex);
    }

    public String toString() {
        return this.pattern.toString();
    }

    private static class InnerToken extends ColorPhrase.Token {
        private final String innerText;
        private int color;

        InnerToken(ColorPhrase.Token var1, String var2, int var3) {
            super(var1);
            this.innerText = var2;
            this.color = var3;
        }

        void expand(SpannableStringBuilder var1) {
            int var2 = this.getFormattedStart();
            int var3 = var2 + this.innerText.length() + 2;
            var1.replace(var2, var3, this.innerText);
            var1.setSpan(new ForegroundColorSpan(this.color), var2, var3 - 2, 33);
        }

        int getFormattedLength() {
            return this.innerText.length();
        }
    }

    private static class LeftSeparatorToken extends ColorPhrase.Token {
        private char leftSeparetor;

        LeftSeparatorToken(ColorPhrase.Token var1, char var2) {
            super(var1);
            this.leftSeparetor = var2;
        }

        void expand(SpannableStringBuilder var1) {
            int var2 = this.getFormattedStart();
            var1.replace(var2, var2 + 2, String.valueOf(this.leftSeparetor));
        }

        int getFormattedLength() {
            return 1;
        }
    }

    private static class OuterToken extends ColorPhrase.Token {
        private final int textLength;
        private int color;

        OuterToken(ColorPhrase.Token var1, int var2, int var3) {
            super(var1);
            this.textLength = var2;
            this.color = var3;
        }

        void expand(SpannableStringBuilder var1) {
            int var2 = this.getFormattedStart();
            int var3 = var2 + this.textLength;
            var1.setSpan(new ForegroundColorSpan(this.color), var2, var3, 33);
        }

        int getFormattedLength() {
            return this.textLength;
        }
    }

    private abstract static class Token {
        private final ColorPhrase.Token prev;
        private ColorPhrase.Token next;

        protected Token(ColorPhrase.Token var1) {
            this.prev = var1;
            if(var1 != null) {
                var1.next = this;
            }

        }

        abstract void expand(SpannableStringBuilder var1);

        abstract int getFormattedLength();

        final int getFormattedStart() {
            return this.prev == null?0:this.prev.getFormattedStart() + this.prev.getFormattedLength();
        }
    }
}